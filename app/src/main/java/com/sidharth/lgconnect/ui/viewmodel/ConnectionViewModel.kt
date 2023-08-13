package com.sidharth.lgconnect.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConnectionViewModel : ViewModel() {
    private val _connectionStatus = MutableLiveData<Boolean>()
    val connectionStatus: LiveData<Boolean> get() = _connectionStatus

    init {
        _connectionStatus.postValue(false)
    }

    fun setConnectionStatus(status: Boolean) {
        _connectionStatus.postValue(status)
    }
}