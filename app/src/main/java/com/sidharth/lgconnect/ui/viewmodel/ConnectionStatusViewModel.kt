package com.sidharth.lgconnect.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConnectionStatusViewModel : ViewModel() {
    private val _connectionStatus = MutableLiveData<Boolean>()

    val connectionStatus: LiveData<Boolean>
        get() = _connectionStatus

    init {
        _connectionStatus.value = false
    }

    fun updateConnectionStatus(status: Boolean) {
        _connectionStatus.postValue(status)
    }
}