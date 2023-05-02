package com.sidharth.lgconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ConnectionStatusViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConnectionStatusViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConnectionStatusViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}