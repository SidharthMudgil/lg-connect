package com.sidharth.lgconnect.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.sidharth.lgconnect.R
import com.sidharth.lgconnect.databinding.FragmentSettingsBinding
import com.sidharth.lgconnect.domain.model.SSHConfig
import com.sidharth.lgconnect.service.SSHService
import com.sidharth.lgconnect.service.ServiceManager
import com.sidharth.lgconnect.ui.viewmodel.ConnectionStatusViewModel
import com.sidharth.lgconnect.util.DialogUtils
import com.sidharth.lgconnect.util.KeyboardUtils
import com.sidharth.lgconnect.util.ResourceProvider
import kotlinx.coroutines.launch

const val HINT_USERNAME: String = "username"
const val HINT_PASSWORD: String = "password"
const val HINT_IP: String = "192.168.1.14"
const val HINT_PORT: String = "8080"

class SettingsFragment : Fragment() {
    private lateinit var resourceProvider: ResourceProvider
    private lateinit var dialog: DialogUtils
    private val viewModel: ConnectionStatusViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resourceProvider = ResourceProvider(requireContext())
        dialog = DialogUtils(context = requireContext(),
            image = resourceProvider.getDrawable(R.drawable.cartoon2),
            title = resourceProvider.getString(R.string.connection_failed_title),
            description = resourceProvider.getString(R.string.connection_failed_description),
            buttonLabel = resourceProvider.getString(R.string.connection_failed_button_text),
            onDialogButtonClick = { dialog.dismiss() })

        ServiceManager.initializeDialog(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSettingsBinding.inflate(inflater)

        binding.etUsername.setOnFocusChangeListener { v, hasFocus ->
            (v as EditText).hint = if (hasFocus) {
                KeyboardUtils.showSoftKeyboard(v)
                HINT_USERNAME
            } else {
                KeyboardUtils.hideSoftKeyboard(v)
                ""
            }
        }

        binding.etPassword.setOnFocusChangeListener { v, hasFocus ->
            (v as EditText).hint = if (hasFocus) {
                KeyboardUtils.showSoftKeyboard(v)
                HINT_PASSWORD
            } else {
                KeyboardUtils.hideSoftKeyboard(v)
                ""
            }
        }

        binding.etIp.setOnFocusChangeListener { v, hasFocus ->
            (v as EditText).hint = if (hasFocus) {
                KeyboardUtils.showSoftKeyboard(v)
                HINT_IP
            } else {
                KeyboardUtils.hideSoftKeyboard(v)
                ""
            }
        }

        binding.etPort.setOnFocusChangeListener { v, hasFocus ->
            (v as EditText).hint = if (hasFocus) {
                KeyboardUtils.showSoftKeyboard(v)
                HINT_PORT
            } else {
                KeyboardUtils.hideSoftKeyboard(v)
                ""
            }
        }

        viewModel.connectionStatus.observe(viewLifecycleOwner) {
            when {
                it -> {
                    binding.tvConnectionStatus.text = resourceProvider.getString(R.string.connected)
                    binding.tvConnectionStatus.setTextColor(resourceProvider.getColor(R.color.lime_green))
                }

                else -> {
                    binding.tvConnectionStatus.text =
                        resourceProvider.getString(R.string.disconnected)
                    binding.tvConnectionStatus.setTextColor(resourceProvider.getColor(R.color.soft_red))
                }
            }
        }

        binding.etPort.setOnEditorActionListener { v, _, _ ->
            KeyboardUtils.hideSoftKeyboard(v)
            connectIfValid(binding)
            true
        }

        binding.mcvConnect.setOnClickListener {
            KeyboardUtils.hideSoftKeyboard(it)
            connectIfValid(binding)
        }

        return binding.root
    }

    private fun connectIfValid(binding: FragmentSettingsBinding) {
        val fields = listOf(
            binding.etUsername to "Username is required",
            binding.etIp to "IP address is required",
            binding.etPort to "Port number is required"
        )

        if (SSHService.validate(fields)) {
            val config = SSHConfig(
                username = binding.etUsername.text.toString().trim(),
                password = binding.etPassword.text.toString(),
                hostname = binding.etIp.text.toString().trim(),
                port = binding.etPort.text.toString().trim().toInt()
            )

            binding.tvLabel.text = getString(R.string.connecting)
            binding.mcvConnect.isClickable = false

            lifecycleScope.launch {
                context?.let {

                    ServiceManager.initialize(context = it, sshConfig = config)
                }

                when (ServiceManager.getSSHService()?.connect()) {
                    true -> {
                        viewModel.updateConnectionStatus(true)
                        binding.tvLabel.text = getString(R.string.connect)
                        binding.mcvConnect.isClickable = true
                    }
                    else -> {
                        viewModel.updateConnectionStatus(false)
                        binding.tvLabel.text = getString(R.string.connect)
                        binding.mcvConnect.isClickable = true
                        dialog.show()
                    }
                }
            }
        }
    }
}