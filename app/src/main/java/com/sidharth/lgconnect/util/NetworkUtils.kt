import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

class NetworkConnectivityCallback(
    private val onConnectionAvailable: () -> Unit,
    private val onConnectionLost: () -> Unit,
) : ConnectivityManager.NetworkCallback() {

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        onConnectionAvailable()
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        onConnectionLost()
    }
}

object NetworkUtils {

    private var connectivityCallback: NetworkConnectivityCallback? = null
    private var noNetworkDialog: AlertDialog? = null

    fun startNetworkCallback(
        context: Context,
        onConnectionAvailable: () -> Unit,
        onConnectionLost: () -> Unit,
    ) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkRequest =
            NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET).build()

        val callback = NetworkConnectivityCallback(onConnectionAvailable, onConnectionLost)
        connectivityManager.registerNetworkCallback(networkRequest, callback)
        connectivityCallback = callback
    }

    fun stopNetworkCallback(context: Context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityCallback?.let {
            connectivityManager.unregisterNetworkCallback(it)
            connectivityCallback = null
        }
    }

    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities != null && (
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }

    fun showNoNetworkDialog(context: Context, onRetry: () -> Unit) {
//        val builder = AlertDialog.Builder(context)
//        builder.setTitle("No internet connection")
//        builder.setMessage("Please check your network settings and try again.")
//        builder.setCancelable(false)
//        builder.setPositiveButton("Retry") { dialog: DialogInterface, _: Int ->
//            if (isNetworkConnected(context)) {
//                // If the device is now connected to the internet, call the onRetry function
//                onRetry.invoke()
//            } else {
//                // If the device is still not connected to the internet, show the dialog again
//                showNoNetworkDialog(context, onRetry)
//            }
//            dialog.dismiss()
//        }
//        builder.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
//            dialog.dismiss()
//        }
//        builder.show()
    }

    fun dismissNoNetworkDialog() {
        noNetworkDialog?.dismiss()
        noNetworkDialog = null
    }
}
