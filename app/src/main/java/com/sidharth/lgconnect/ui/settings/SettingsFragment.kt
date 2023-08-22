package com.sidharth.lgconnect.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.sidharth.lgconnect.R
import com.sidharth.lgconnect.databinding.FragmentSettingsBinding
import com.sidharth.lgconnect.ui.viewmodel.ConnectionViewModel
import com.sidharth.lgconnect.util.FormValidator
import com.sidharth.lgconnect.util.KeyboardUtils
import com.sidharth.lgconnect.util.LGDialogs
import com.sidharth.lgconnect.util.LGManager
import com.sidharth.lgconnect.util.ResourceProvider
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private lateinit var resourceProvider: ResourceProvider
    private lateinit var binding: FragmentSettingsBinding
    private var lgDialogs: LGDialogs? = null
    private val connectionViewModel: ConnectionViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resourceProvider = ResourceProvider(requireContext())
        lgDialogs = LGDialogs()
    }

    override fun onPause() {
        super.onPause()
        lgDialogs?.dismissNoConnectionDialog()
        lgDialogs?.dismissConnectionFailedDialog()
        lgDialogs = null
    }

    override fun onDestroy() {
        super.onDestroy()
        lgDialogs?.dismissNoConnectionDialog()
        lgDialogs?.dismissConnectionFailedDialog()
        lgDialogs = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)

        connectionViewModel.connectionStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                true -> onConnected()
                else -> onDisconnected()
            }
        }

        binding.mcvConnect.setOnClickListener {
            KeyboardUtils.hideSoftKeyboard(it)
            when (connectionViewModel.connectionStatus.value) {
                true -> disconnect()
                else -> connectIfValid(binding)
            }
        }

        return binding.root
    }

    private fun disconnect() {
        lifecycleScope.launch {
            LGManager.getInstance()?.disconnect()
            connectionViewModel.setConnectionStatus(false)
            onDisconnected()
        }
    }

    private fun connectIfValid(binding: FragmentSettingsBinding) {
        val fields = listOf(
            binding.etUsername to "Username is required",
            binding.etIp to "IP address is required",
            binding.etPort to "Port number is required"
        )

        lifecycleScope.launch {
            LGManager.getInstance()?.disconnect()
            connectionViewModel.setConnectionStatus(false)
            onDisconnected()
        }

        if (FormValidator.validate(fields)) {
            binding.tvLabel.text = getString(R.string.connecting)
            binding.mcvConnect.isClickable = false

            lifecycleScope.launch {
                LGManager.newInstance(
                    username = binding.etUsername.text.toString().trim(),
                    password = binding.etPassword.text.toString(),
                    host = binding.etIp.text.toString().trim(),
                    port = binding.etPort.text.toString().trim().toInt(),
                    screens = 3
                )

                lifecycleScope.launch {
                    when (LGManager.getInstance()?.connect() == true) {
                        true -> {
                            connectionViewModel.setConnectionStatus(true)
                            onConnected()
                        }

                        else -> {
                            connectionViewModel.setConnectionStatus(false)
                            onDisconnected()
                            lgDialogs?.showConnectionFailedDialog(requireContext())
                        }
                    }
                    binding.mcvConnect.isClickable = true
                }
            }
        }
    }

    private fun onConnected() {
        binding.tvLabel.text = getString(R.string.disconnect)
        binding.tvConnectionStatus.text = resourceProvider.getString(R.string.connected)
        binding.tvConnectionStatus.setTextColor(resourceProvider.getColor(R.color.lime_green))
    }

    private fun onDisconnected() {
        binding.tvLabel.text = getString(R.string.connect)
        binding.tvConnectionStatus.text = resourceProvider.getString(R.string.disconnected)
        binding.tvConnectionStatus.setTextColor(resourceProvider.getColor(R.color.soft_red))
    }
}