package com.sidharth.lgconnect.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FileService(private val context: Context) {

    private fun getKmlDir(): File {
        val dir = File(context.getExternalFilesDir(null), "KML")
        if (!dir.exists()) dir.mkdir()
        return dir
    }

    suspend fun createFile(name: String, data: String): File? {
        return withContext(Dispatchers.IO) {
            try {
                val fileDir = getKmlDir()
                val file = File(fileDir, name)
                val fos = FileOutputStream(file)
                fos.write(data.toByteArray())
                fos.close()
                file
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun createImage(name: String, path: String): File? {
        return withContext(Dispatchers.IO) {
            val file = try {
                val options = BitmapFactory.Options()
                options.inPreferredConfig = Bitmap.Config.ARGB_8888
                val bitmap = BitmapFactory.decodeFile(path, options)
                val file1 =
                    File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$name.jpg")
                val fos = FileOutputStream(file1)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
                fos.close()
                file1
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
            file
        }
    }
}
