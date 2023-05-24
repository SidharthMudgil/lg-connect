package com.sidharth.lgconnect.service

import com.sidharth.lgconnect.domain.model.Marker
import kotlinx.coroutines.runBlocking

class LGService(
    private val sshService: SSHService,
    private val fileService: FileService,
) {
    private var totalScreens = 5

    private val leftScreen: Int
        get() {
            return when (totalScreens) {
                1 -> 1
                else -> totalScreens / 2 + 2
            }
        }

    private val rightScreen: Int
        get() {
            return when (totalScreens) {
                1 -> 1
                else -> totalScreens / 2 + 1
            }
        }

    init {
        runBlocking {
            totalScreens = Integer.parseInt(getScreenAmount())
        }
    }

    suspend fun setRefresh() {
        val search = "<href>##LG_PHPIFACE##kml\\/slave_{{slave}}.kml<\\/href>"
        val replace =
            "<href>##LG_PHPIFACE##kml\\/slave_{{slave}}.kml<\\/href><refreshMode>onInterval<\\/refreshMode><refreshInterval>2<\\/refreshInterval>"

        val setCommand =
            "echo ${sshService.passwordOrKey} | sudo -S sed -i \"s/$search/$replace/\" ~/earth/kml/slave/myplaces.kml"

        val clearCommand =
            "echo ${sshService.passwordOrKey} | sudo -S sed -i \"s/$replace/$search/\" ~/earth/kml/slave/myplaces.kml"

        for (i in 2..totalScreens) {
            val clearCmd = clearCommand.replace("{{slave}}", i.toString())
            val setCmd = setCommand.replace("{{slave}}", i.toString())
            val query = "sshpass -p ${sshService.passwordOrKey} ssh -t lg$i \'{{cmd}}\'"

            sshService.execute(query.replace("{{cmd}}", clearCmd))
            sshService.execute(query.replace("{{cmd}}", setCmd))
        }
    }

    suspend fun resetRefresh() {
        val search =
            "<href>##LG_PHPIFACE##kml\\/slave_{{slave}}.kml<\\/href><refreshMode>onInterval<\\/refreshMode><refreshInterval>2<\\/refreshInterval>"
        val replace = "<href>##LG_PHPIFACE##kml\\/slave_{{slave}}.kml<\\/href>"

        val clear =
            "echo ${sshService.passwordOrKey} | sudo -S sed -i \"s/$search/$replace/\" ~/earth/kml/slave/myplaces.kml"

        for (i in 2..totalScreens) {
            val cmd = clear.replace("{{slave}}", i.toString())
            sshService.execute("sshpass -p ${sshService.passwordOrKey} ssh -t lg$i \'$cmd\'")
        }

        reboot()
    }

    suspend fun clearKml() {
        val blank = """
            <?xml version="1.0" encoding="UTF-8"?>
            <kml xmlns="http://www.opengis.net/kml/2.2" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:kml="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom">
              <Document id="{{id}}">
              </Document>
            </kml>
        """.trimIndent()

        var query = "echo 'exittour=true' > /tmp/query.txt && > /var/www/html/kmls.txt"

        for (i in 2..totalScreens) {
            val blankKml = blank.replace("{{id}}", "slave_$i")
            query += " && echo '$blankKml' > /var/www/html/kml/slave_$i.kml"
        }

//        TODO implement if keepLogos = true

        sshService.execute(query)
    }

    suspend fun relaunch() {
        for (i in 1..totalScreens) {
            val relaunchCommand = """
            RELAUNCH_CMD="\\
            if [ -f /etc/init/lxdm.conf ]; then
              export SERVICE=lxdm
            elif [ -f /etc/init/lightdm.conf ]; then
              export SERVICE=lightdm
            else
              exit 1
            fi
            if  [[ $(service ${'$'}SERVICE status) =~ 'stop' ]]; then
              echo ${sshService.passwordOrKey} | sudo -S service ${'$'}{SERVICE} start
            else
              echo ${sshService.passwordOrKey} | sudo -S service ${'$'}{SERVICE} restart
            fi
            " && sshpass -p ${sshService.passwordOrKey} ssh -x -t lg@lg\$i "${'$'}RELAUNCH_CMD\"
        """.trimIndent()

            sshService.execute("\"/home/${sshService.user}/bin/lg-relaunch\" > /home/${sshService.user}/log.txt")
            sshService.execute(relaunchCommand)
        }
    }


    suspend fun reboot() {
        for (i in 1..totalScreens) {
            sshService.execute("sshpass -p ${sshService.passwordOrKey} ssh -t lg$i \"echo ${sshService.passwordOrKey} | sudo -S reboot\"")
        }
    }

    suspend fun powerOff() {
        for (i in 1..totalScreens) {
            sshService.execute("sshpass -p ${sshService.passwordOrKey} ssh -t lg$i \"echo ${sshService.passwordOrKey} | sudo -S poweroff\"")
        }
    }

    //    '1492.665945696469',
//        '45',
//        '0'
    suspend fun flyTo(place: String) {
        sshService.execute("echo 'search=${place}' > /tmp/query.txt`)")
    }

    suspend fun lookAt(marker: Marker) {
        val kml = KMLService.lookAt(marker)
        sshService.execute("")
    }

    suspend fun changePlanet(name: String) {
        sshService.execute("echo 'planet=${name}' > /tmp/query.txt")
    }

    suspend fun startOrbit() {
        sshService.execute("echo 'playtour=Orbit' > /tmp/query.txt")
    }

    suspend fun stopOrbit() {
        sshService.execute("echo 'exittour=true' > /tmp/query.txt")
    }

    private suspend fun getScreenAmount(): String {
        val out = sshService.execute("grep -oP '(?<=DHCP_LG_FRAMES_MAX=).*' personavars.txt")
        return when {
            out.isEmpty() -> "1"
            else -> out[0]
        }
    }

    suspend fun createShowChart(type: String) {
        val kml = KMLService.createChartKML(type)
        sshService.execute("echo '${kml}' > /var/www/html/kml/slave_${rightScreen}.kml")
    }

    suspend fun createShowMarker(marker: Marker) {
        val kml: String = KMLService.createMarker(marker)
        sshService.execute("") // TODO(enter command)
    }

    suspend fun setLogo() {
        val kml: String = KMLService.createLogo("https://i.imgur.com/6BgpZq7.png")
        sshService.execute("echo '${kml}' > /var/www/html/kml/slave_$leftScreen.kml")
    }

    suspend fun sendKml(kml: String) {
        sshService.execute("") // TODO(enter command)
    }
}
