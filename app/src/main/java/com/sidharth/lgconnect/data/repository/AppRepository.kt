package com.sidharth.lgconnect.data.repository

import android.content.Context
import com.sidharth.lgconnect.data.local.AppDatabase
import com.sidharth.lgconnect.data.local.LocalDataSourceImpl
import com.sidharth.lgconnect.domain.repository.DataRepository

object AppRepository {
    private var dataRepository: DataRepository? = null

    fun getInstance(context: Context): DataRepository {
        return dataRepository ?: DataRepositoryImpl(
            LocalDataSourceImpl(
                AppDatabase.getDatabase(
                    context
                ).markerDao()
            )
        ).apply {
                dataRepository = this
            }
    }
}
