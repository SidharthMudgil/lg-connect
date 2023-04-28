import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

class FileService(private val context: Context) {

    private fun getKmlDir(): File {
        val dir = File(context.getExternalFilesDir(null), "KML")
        if (!dir.exists()) dir.mkdir()
        return dir
    }

    fun createFile(name: String, data: String): File {
        val fileDir = getKmlDir()
        val file = File(fileDir, name)
        val fos = FileOutputStream(file)
        fos.write(data.toByteArray())
        fos.close()
        return file
    }

    fun createImage(name: String, path: String): File? {
        try {
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            val bitmap = BitmapFactory.decodeFile(path, options)
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$name.jpg")
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
            fos.close()
            return file
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
