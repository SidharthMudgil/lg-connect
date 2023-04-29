class LGService(
    private val sshService: SSHService,
    private val fileService: FileService,
) {
    val totalScreens = 5

    fun setRefresh() {
        val search = "<href>##LG_PHPIFACE##kml\\/slave_{{slave}}.kml<\\/href>";
        val replace =
            "<href>##LG_PHPIFACE##kml\\/slave_{{slave}}.kml<\\/href><refreshMode>onInterval<\\/refreshMode><refreshInterval>2<\\/refreshInterval>";

        val setCommand =
            "echo ${sshService.passwordOrKey} | sudo -S sed -i \"s/$search/$replace/\" ~/earth/kml/slave/myplaces.kml";

        val clearCommand =
            "echo ${sshService.passwordOrKey} | sudo -S sed -i \"s/$replace/$search/\" ~/earth/kml/slave/myplaces.kml";

        for (i in 2..totalScreens) {
            val clearCmd = clearCommand.replace("{{slave}}", i.toString())
            val setCmd = setCommand.replace("{{slave}}", i.toString())
            val query = "sshpass -p ${sshService.passwordOrKey} ssh -t lg$i \'{{cmd}}\'"

            sshService.execute(query.replace("{{cmd}}", clearCmd))
            sshService.execute(query.replace("{{cmd}}", setCmd))
        }
    }

    fun resetRefresh() {
        val search =
            "<href>##LG_PHPIFACE##kml\\/slave_{{slave}}.kml<\\/href><refreshMode>onInterval<\\/refreshMode><refreshInterval>2<\\/refreshInterval>"
        val replace = "<href>##LG_PHPIFACE##kml\\/slave_{{slave}}.kml<\\/href>"

        val clear =
            "echo ${sshService.passwordOrKey} | sudo -S sed -i \"s/$search/$replace/\" ~/earth/kml/slave/myplaces.kml"

        for (i in 2..totalScreens) {
            val cmd = clear.replace("{{slave}}", i.toString());
            sshService.execute("sshpass -p ${sshService.passwordOrKey} ssh -t lg$i \'$cmd\'")
        }

        reboot()
    }

    fun clearKml() {
        val blank = """
            <?xml version="1.0" encoding="UTF-8"?>
            <kml xmlns="http://www.opengis.net/kml/2.2" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:kml="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom">
              <Document id="{{id}}">
              </Document>
            </kml>
        """.trimIndent()

        var query = "echo \"exittour=true\" > /tmp/query.txt && > /var/www/html/kmls.txt"

        for (i in 2..totalScreens) {
            val blankKml = blank.replace("{{id}}", "slave_$i")
            query += " && echo '$blankKml' > /var/www/html/kml/slave_$i.kml"
        }

//        TODO implement if keepLogos = true

        sshService.execute(query)
    }

    fun relaunch() {
        for (i in 1..totalScreens) {
            val relaunchCommand = """RELAUNCH_CMD="\\
if [ -f /etc/init/lxdm.conf ]; then
  export SERVICE=lxdm
elif [ -f /etc/init/lightdm.conf ]; then
  export SERVICE=lightdm
else
  exit 1
fi
if  [[ \\\$(service \\${'$'}SERVICE status) =~ 'stop' ]]; then
  echo \\${sshService.passwordOrKey} | sudo -S service \\${'$'}{SERVICE} start
else
  echo \\${sshService.passwordOrKey} | sudo -S service \\${'$'}{SERVICE} restart
fi
" && sshpass -p \\${sshService.passwordOrKey} ssh -x -t lg@lg\$i "\${'$'}RELAUNCH_CMD\"""".trimIndent()

            sshService.execute("\"/home/${sshService.user}/bin/lg-relaunch\" > /home/${sshService.user}/log.txt")
            sshService.execute(relaunchCommand)
        }
    }

    fun reboot() {
        for (i in 1..totalScreens) {
            sshService.execute("sshpass -p ${sshService.passwordOrKey} ssh -t lg$i \"echo ${sshService.passwordOrKey} | sudo -S reboot\"")
        }
    }

    fun powerOff() {
        for (i in 1..totalScreens) {
            sshService.execute("sshpass -p ${sshService.passwordOrKey} ssh -t lg$i \"echo ${sshService.passwordOrKey} | sudo -S poweroff\"")
        }
    }

    fun setLogos() {
        // implementation for setLogos
    }

    fun sendKml() {

    }
}
