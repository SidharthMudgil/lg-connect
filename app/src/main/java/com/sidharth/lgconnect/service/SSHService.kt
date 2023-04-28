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

    fun connect() {
        ssh.connect(hostname, port)
        ssh.authPassword(username, password)
    }

    fun disconnect() {
        ssh.disconnect()
    }

    fun executeCommand(command: String): List<String> {
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
        return output
    }

    fun uploadFile(data: String, remotePath: String): Boolean {
        val sftp = ssh.newSFTPClient()
        return try {
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
