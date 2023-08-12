package com.sidharth.lgconnect.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.sidharth.lgconnect.R
import com.sidharth.lgconnect.databinding.FragmentSettingsBinding
import com.sidharth.lgconnect.util.FormValidator
import com.sidharth.lgconnect.util.KeyboardUtils
import com.sidharth.lgconnect.util.LGDialogs
import com.sidharth.lgconnect.util.LGManager
import com.sidharth.lgconnect.util.ResourceProvider
import kotlinx.coroutines.launch

const val HINT_USERNAME: String = "username"
const val HINT_PASSWORD: String = "password"
const val HINT_IP: String = "192.168.1.14"
const val HINT_PORT: String = "8080"

class SettingsFragment : Fragment() {
    private lateinit var resourceProvider: ResourceProvider
    private lateinit var binding: FragmentSettingsBinding
    private var lgDialogs: LGDialogs? = null

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
                        true -> onConnected()
                        else -> onDisconnected()
                    }
                    binding.mcvConnect.isClickable = true
                }
            }
        }
    }

    private fun onConnected() {
        binding.tvConnectionStatus.text = resourceProvider.getString(R.string.connected)
        binding.tvConnectionStatus.setTextColor(resourceProvider.getColor(R.color.lime_green))
    }

    private fun onDisconnected() {
        binding.tvConnectionStatus.text = resourceProvider.getString(R.string.disconnected)
        binding.tvConnectionStatus.setTextColor(resourceProvider.getColor(R.color.soft_red))
    }
}