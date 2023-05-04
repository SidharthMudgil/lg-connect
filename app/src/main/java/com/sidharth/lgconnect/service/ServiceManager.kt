package com.sidharth.lgconnect.service

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import com.sidharth.lgconnect.R
import com.sidharth.lgconnect.domain.model.SSHConfig
import com.sidharth.lgconnect.util.DialogUtils
import java.lang.ref.WeakReference

object ServiceManager {
    private var sshService: SSHService? = null
    private var fileService: FileService? = null
    private var lgService: LGService? = null

    private var contextRef: WeakReference<Context>? = null
    private var dialog: DialogUtils? = null

    fun initializeDialog(context: Context) {
        dialog = DialogUtils(
            context = context,
            image = AppCompatResources.getDrawable(context, R.drawable.cartoon3)!!,
            title = context.getString(R.string.no_connection_title),
            description = context.getString(R.string.no_connection_description),
            buttonLabel = context.getString(R.string.no_connection_button_text),
            onDialogButtonClick = { dialog?.dismiss() }
        )
    }

    fun initialize(
        context: Context,
        username: String,
        password: String,
        hostname: String,
        port: Int,
    ) {
        initializeDialog(context)
        contextRef = WeakReference(context.applicationContext)
        fileService = FileService(context)
        sshService = SSHService(hostname, port, username, password)
        lgService = LGService(sshService!!, fileService!!)
    }

    fun initialize(
        context: Context,
        sshConfig: SSHConfig,
    ) {
        initializeDialog(context)
        contextRef = WeakReference(context.applicationContext)
        fileService = FileService(context)
        sshService = SSHService(
            hostname = sshConfig.hostname,
            port = sshConfig.port,
            username = sshConfig.username,
            password = sshConfig.password
        )
        lgService = LGService(sshService!!, fileService!!)
    }

    fun initialize(context: Context, fileService: FileService, sshService: SSHService) {
        initializeDialog(context)
        contextRef = WeakReference(context.applicationContext)
        this.fileService = fileService
        this.sshService = sshService
        lgService = LGService(sshService, fileService)
    }

    fun getLGService(): LGService? {
        return lgService
    }

    fun getFileService(): FileService? {
        return fileService
    }

    fun getSSHService(): SSHService? {
        return sshService
    }

    fun showNoConnectionDialog() {
        dialog!!.show()
    }
}
