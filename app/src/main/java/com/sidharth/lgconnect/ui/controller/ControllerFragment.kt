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
import com.sidharth.lgconnect.service.LGService
import com.sidharth.lgconnect.service.ServiceManager
import com.sidharth.lgconnect.ui.viewmodel.ConnectionStatusViewModel
import com.sidharth.lgconnect.util.DialogUtils
import com.sidharth.lgconnect.util.ResourceProvider
import kotlinx.coroutines.launch


class ControllerFragment : Fragment() {
    private lateinit var resourceProvider: ResourceProvider
    private lateinit var dialogUtils: DialogUtils
    private var lgService: LGService? = null
    private val viewModel: ConnectionStatusViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resourceProvider = ResourceProvider(requireContext())
        dialogUtils = DialogUtils(context = requireContext(),
            image = resourceProvider.getDrawable(R.drawable.cartoon3),
            title = resourceProvider.getString(R.string.no_connection_title),
            description = resourceProvider.getString(R.string.no_connection_description),
            buttonLabel = resourceProvider.getString(R.string.no_connection_button_text),
            onDialogButtonClick = { dialogUtils.dismiss() })
        if (ServiceManager.getSSHService()?.isConnected == true) {
            lgService = ServiceManager.getLGService()
        }
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
                lgService?.setRefresh() ?: context?.let {
                    dialogUtils.show()
                }
            }
        }

        binding.mcvSetSlaveRefresh.setOnClickListener {
            lifecycleScope.launch {
                lgService?.setRefresh() ?: context?.let {
                    dialogUtils.show()
                }
            }
        }

        binding.mcvResetSlaveRefresh.setOnClickListener {
            lifecycleScope.launch {
                lgService?.resetRefresh() ?: context?.let {
                    dialogUtils.show()
                }
            }
        }

        binding.mcvClearKml.setOnClickListener {
            lifecycleScope.launch {
                lgService?.clearKml() ?: context?.let {
                    dialogUtils.show()
                }
            }
        }

        binding.mcvRelaunch.setOnClickListener {
            lifecycleScope.launch {
                lgService?.relaunch() ?: context?.let {
                    dialogUtils.show()
                }
            }
        }

        binding.mcvReboot.setOnClickListener {
            lifecycleScope.launch {
                lgService?.reboot() ?: context?.let {
                    dialogUtils.show()
                }
            }
        }

        binding.mcvPowerOff.setOnClickListener {
            lifecycleScope.launch {
                lgService?.powerOff() ?: context?.let {
                    dialogUtils.show()
                }
            }
        }

        return binding.root
    }
}