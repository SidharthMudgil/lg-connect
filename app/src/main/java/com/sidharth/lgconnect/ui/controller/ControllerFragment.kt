package com.sidharth.lgconnect.ui.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.sidharth.lgconnect.R
import com.sidharth.lgconnect.databinding.FragmentControllerBinding
import com.sidharth.lgconnect.service.ServiceManager
import com.sidharth.lgconnect.ui.viewmodel.ConnectionStatusViewModel
import com.sidharth.lgconnect.util.DialogUtils
import com.sidharth.lgconnect.util.ResourceProvider
import kotlinx.coroutines.launch


class ControllerFragment : Fragment() {
    private lateinit var resourceProvider: ResourceProvider
    private lateinit var dialog: DialogUtils
    private val viewModel: ConnectionStatusViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resourceProvider = ResourceProvider(requireContext())
        dialog = DialogUtils(context = requireContext(),
            image = resourceProvider.getDrawable(R.drawable.cartoon3),
            title = resourceProvider.getString(R.string.no_connection_title),
            description = resourceProvider.getString(R.string.no_connection_description),
            buttonLabel = resourceProvider.getString(R.string.no_connection_button_text),
            onDialogButtonClick = { dialog.dismiss() })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentControllerBinding.inflate(inflater)

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

        binding.mcvSetSlaveRefresh.setOnClickListener {
            lifecycleScope.launch {
                execute(
                    action = { ServiceManager.getLGService()?.setRefresh() ?: dialog.show() },
                    onFailure = { dialog.show() })
            }
        }

        binding.mcvResetSlaveRefresh.setOnClickListener {
            lifecycleScope.launch {
                execute(
                    action = { ServiceManager.getLGService()?.resetRefresh() ?: dialog.show() },
                    onFailure = { dialog.show() })
            }
        }

        binding.mcvClearKml.setOnClickListener {
            lifecycleScope.launch {
                execute(
                    action = { ServiceManager.getLGService()?.clearKml() ?: dialog.show() },
                    onFailure = { dialog.show() })
            }
        }

        binding.mcvRelaunch.setOnClickListener {
            lifecycleScope.launch {
                execute(
                    action = { ServiceManager.getLGService()?.relaunch() ?: dialog.show() },
                    onFailure = { dialog.show() })
            }
        }

        binding.mcvReboot.setOnClickListener {
            lifecycleScope.launch {
                execute(
                    action = { ServiceManager.getLGService()?.reboot() ?: dialog.show() },
                    onFailure = { dialog.show() })
            }
        }

        binding.mcvPowerOff.setOnClickListener {
            lifecycleScope.launch {
                execute(action = { ServiceManager.getLGService()?.powerOff() ?: dialog.show() },
                    onFailure = { dialog.show() })
            }
        }

        return binding.root
    }

    private suspend fun execute(action: suspend () -> Unit, onFailure: () -> Unit) {
        try {
            when (viewModel.connectionStatus.value) {
                true -> action()
                false -> onFailure()
                else -> onFailure()
            }
        } catch (e: Exception) {
            onFailure()
        }
    }
}