package com.sidharth.lgconnect.util

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import com.sidharth.lgconnect.domain.model.Marker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Properties


class LGManager(
    private val username: String,
    private val password: String,
    private val host: String,
    private val port: Int,
    private val screens: Int,
) {
    private val logoSlave: String get() = "slave_${if (screens == 1) 1 else (screens / 2) + 2}"
    private val dataSlave: String get() = "slave_${if (screens == 1) 1 else (screens / 2) + 1}"
    private var session: Session? = null

    val connected: Boolean
        get() {
            return session?.isConnected ?: false
        }

    private suspend fun setupNewSession(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val jSch = JSch()
                session = jSch.getSession(username, host, port)
                session?.setPassword(password)
                val config = Properties()
                config["StrictHostKeyChecking"] = "no"
                session?.setConfig(config)
                session?.connect(7000)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun connect(): Boolean {
        return try {
            if (session == null || connected.not()) {
                if (setupNewSession()) {
                    displayLogos()
                } else {
                    return false
                }
            } else {
                session?.sendKeepAliveMsg()
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun disconnect() {
        withContext(Dispatchers.IO) {
            try {
                if (session != null) {
                    session?.disconnect()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                session = null
            }
        }
    }

    private suspend fun execute(command: String) {
        withContext(Dispatchers.IO) {
            try {
                if (session != null && connected) {
                    val channel = session?.openChannel("exec") as ChannelExec
                    channel.setCommand(command)
                    channel.connect()
                    channel.disconnect()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun displayLogos() {
        val command =
            "chmod 777 /var/www/html/kml/$logoSlave.kml; echo '${KMLUtils.screenOverlayImage()}' > /var/www/html/kml/$logoSlave.kml"
        execute(command)
    }

    suspend fun clearKml() {
        for (i in 2..screens) {
            execute("chmod 777 /var/www/html/kml/slave_$i.kml; echo '' > /var/www/html/kml/slave_$i.kml")
        }
        execute("echo '' > /tmp/query.txt")
        execute("echo '' > /var/www/html/kmls.txt")
    }

    private suspend fun clearData() {
        execute("chmod 777 /var/www/html/kml/$dataSlave.kml; echo '' > /var/www/html/kml/${dataSlave}}.kml")
    }

    suspend fun sendKml(kml: String, refresh: Boolean = true) {
        if (refresh) {
            clearData()
        }
        execute("echo '\n$kml' > /var/www/html/kmls.txt")
    }

    private suspend fun sendKmlToSlave(kml: String) {
        execute("chmod 777 /var/www/html/kml/$dataSlave.kml; echo '\n$kml' > /var/www/html/kml/${dataSlave}.kml")
    }

    suspend fun showMarkers(markers: List<Marker>) {
        execute("echo '${KMLUtils.generateMarkersKml(markers)}' > /var/www/html/kmls.txt")
    }

    suspend fun changePlanet(planet: String) {
        execute("echo 'planet=$planet' > /tmp/query.txt")
    }

    suspend fun showChart(type: String) {
        sendKmlToSlave(KMLUtils.createChartKML(type))
    }

    suspend fun orbitAround(latLng: LatLng) {
        execute("echo 'flytoview=${KMLUtils.orbitAround(latLng)}' > /tmp/query.txt")
    }

    suspend fun flyTo(cameraPosition: CameraPosition) {
        execute("echo 'flytoview=${KMLUtils.lookAt(cameraPosition)}' > /tmp/query.txt")
    }

    suspend fun setRefresh() {
        for (i in 2..screens) {
            val search = "<href>##LG_PHPIFACE##kml/slave_$i.kml</href>"
            val replace =
                "<href>##LG_PHPIFACE##kml/slave_$i.kml</href><refreshMode>onInterval</refreshMode><refreshInterval>2</refreshInterval>"
            execute("""sshpass -p $password ssh -t lg$i 'echo $password | sudo -S sed -i "s|$replace|$search|" ~/earth/kml/slave/myplaces.kml'""")
            execute("""sshpass -p $password ssh -t lg$i 'echo $password | sudo -S sed -i "s|$search|$replace|" ~/earth/kml/slave/myplaces.kml'""")
        }
    }

    suspend fun resetRefresh() {
        for (i in 2..screens) {
            val search =
                "<href>##LG_PHPIFACE##kml\\/slave_$i.kml<\\/href><refreshMode>onInterval<\\/refreshMode><refreshInterval>2<\\/refreshInterval>"
            val replace = "<href>##LG_PHPIFACE##kml\\/slave_$i.kml<\\/href>"
            execute(
                """sshpass -p $password ssh -t lg$i 'echo $password | sudo -S sed -i "s|${
                    Regex.escape(
                        search
                    )
                }|$replace|" ~/earth/kml/slave/myplaces.kml'"""
            )
        }
    }

    suspend fun relaunch() {
        for (i in screens downTo 1) {
            val command = """/home/$username/bin/lg-relaunch > /home/$username/log.txt;
                RELAUNCH_CMD="if [ -f /etc/init/lxdm.conf ]; then
                    export SERVICE=lxdm
                elif [ -f /etc/init/lightdm.conf ]; then
                    export SERVICE=lightdm
                else
                    exit 1
                fi
                if  [[ \${'$'}(service \${'$'}SERVICE status) =~ 'stop' ]]; then
                    echo $password | sudo -S service \${'$'}SERVICE start
                else
                    echo $password | sudo -S service \${'$'}SERVICE restart
                fi
                " && sshpass -p $password ssh -x -t lg@lg$i "${'$'}RELAUNCH_CMD"""".trimMargin()

            execute(command)
        }
    }

    suspend fun restart() {
        for (i in screens downTo 1) {
            execute("""sshpass -p $password ssh -t lg$i "echo $password | sudo -S reboot"""")
        }
    }

    suspend fun shutdown() {
        for (i in screens downTo 1) {
            execute("""sshpass -p $password ssh -t lg$i "echo $password | sudo -S poweroff"""")
        }
    }

    suspend fun uploadFile(name: String, file: File) {
        withContext(Dispatchers.IO) {
            if (session != null && connected) {
                val channel = session?.openChannel("sftp") as ChannelSftp
                channel.connect()
                try {
                    val remotePath = "/var/www/html/$name.kml"
                    channel.put(file.absolutePath, remotePath)
                    channel.chmod(644, remotePath)
                } catch (e: Exception) {
                    e.printStackTrace()
                    channel.disconnect()
                } finally {
                    channel.disconnect()
                }
            }
        }
    }

    suspend fun deleteFile(name: String) {
        withContext(Dispatchers.IO) {
            if (session != null && connected) {
                val channel = session?.openChannel("sftp") as ChannelSftp
                channel.connect()
                try {
                    channel.rm("/var/www/html/$name")
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    channel.disconnect()
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LGManager? = null

        fun newInstance(
            username: String,
            password: String,
            host: String,
            port: Int,
            screens: Int,
        ) {
            synchronized(this) {
                INSTANCE = LGManager(
                    username = username,
                    password = password,
                    host = host,
                    port = port,
                    screens = screens,
                ).also {
                    INSTANCE = it
                }
            }
        }

        fun getInstance(): LGManager? {
            return INSTANCE
        }
    }
}