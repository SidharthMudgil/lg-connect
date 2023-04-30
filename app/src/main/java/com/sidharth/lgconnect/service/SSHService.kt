package com.sidharth.lgconnect.service

import android.text.InputType
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.connection.ConnectionException
import net.schmizz.sshj.connection.channel.direct.Session
import net.schmizz.sshj.sftp.SFTPException
import net.schmizz.sshj.transport.verification.HostKeyVerifier
import java.io.IOException
import java.security.PublicKey

class SSHService(
    private val hostname: String,
    private val port: Int,
    private val username: String,
    private val password: String,
) {
    private val ssh = SSHClient()

    val user: String get() = username
    val passwordOrKey: String get() = password

    val isConnected: Boolean get() = ssh.isConnected

    init {
        val hostKeyVerifier = object : HostKeyVerifier {
            override fun verify(hostname: String?, port: Int, key: PublicKey?): Boolean {
                return true
            }

            override fun findExistingAlgorithms(hostname: String?, port: Int): MutableList<String> {
                return mutableListOf()
            }
        }

        ssh.addHostKeyVerifier(hostKeyVerifier)
    }

    suspend fun connect(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                ssh.connect(hostname, port)
                ssh.authPassword(username, password)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun disconnect(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                ssh.disconnect()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun execute(command: String): List<String> {
        return withContext(Dispatchers.IO) {
            val session: Session = ssh.startSession()
            val output: MutableList<String> = ArrayList()
            try {
                session.exec(command).use { cmd ->
                    cmd.inputStream.reader().useLines { lines ->
                        lines.forEach { line -> output.add(line) }
                    }
                }
            } catch (e: ConnectionException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                session.close()
            }
            output
        }
    }

    suspend fun upload(data: String, remotePath: String): Boolean {
        return withContext(Dispatchers.IO) {
            val sftp = ssh.newSFTPClient()
            try {
                sftp.put(data, remotePath)
                true
            } catch (e: SFTPException) {
                e.printStackTrace()
                false
            } finally {
                sftp.close()
            }
        }
    }

    companion object FormValidator {
        fun validate(fields: List<Pair<TextInputEditText, String>>): Boolean {
            var hasError = false
            fields.forEach { (field, errorMessage) ->
                val value = field.text.toString().trim()
                if (value.isEmpty()) {
                    field.error = errorMessage
                    hasError = true
                } else {
                    field.error =
                        if (field.inputType == InputType.TYPE_CLASS_PHONE && !isValidIpAddress(value)) {
                            "Invalid IP address"
                        } else {
                            null
                        }
                }
            }
            return !hasError
        }

        private fun isValidIpAddress(ip: String): Boolean {
            val pattern = Regex(
                "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$"
            )
            return pattern.matches(ip)
        }
    }
}
