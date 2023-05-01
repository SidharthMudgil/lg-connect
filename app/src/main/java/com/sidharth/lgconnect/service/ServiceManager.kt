package com.sidharth.lgconnect.service

import android.content.Context
import com.sidharth.lgconnect.domain.model.SSHConfig
import java.lang.ref.WeakReference

object ServiceManager {
    private var sshService: SSHService? = null
    private var fileService: FileService? = null
    private var lgService: LGService? = null

    private var contextRef: WeakReference<Context>? = null

    fun initialize(
        context: Context,
        username: String,
        password: String,
        hostname: String,
        port: Int,
    ) {
        contextRef = WeakReference(context.applicationContext)
        fileService = FileService(context)
        sshService = SSHService(hostname, port, username, password)
        lgService = LGService(sshService!!, fileService!!)
    }

    fun initialize(
        context: Context,
        sshConfig: SSHConfig,
    ) {
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
}
