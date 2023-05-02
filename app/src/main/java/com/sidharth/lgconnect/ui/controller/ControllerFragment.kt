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
import com.sidharth.lgconnect.util.LGConnectionDialog
import com.sidharth.lgconnect.util.ResourceProvider
import kotlinx.coroutines.launch


class ControllerFragment : Fragment() {
    private lateinit var resourceProvider: ResourceProvider
    private var lgService: LGService? = null
    private val viewModel: ConnectionStatusViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resourceProvider = ResourceProvider(requireContext())
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
                lgService?.setRefresh() ?: context?.let { it1 ->
                    LGConnectionDialog.show(it1) {

                    }
                }
            }
        }

        binding.mcvSetSlaveRefresh.setOnClickListener {
            lifecycleScope.launch {
                lgService?.setRefresh() ?: context?.let { it1 ->
                    LGConnectionDialog.show(it1) {

                    }
                }
            }
        }

        binding.mcvResetSlaveRefresh.setOnClickListener {
            lifecycleScope.launch {
                lgService?.resetRefresh() ?: context?.let { it1 ->
                    LGConnectionDialog.show(it1) {

                    }
                }
            }
        }

        binding.mcvClearKml.setOnClickListener {
            lifecycleScope.launch {
                lgService?.clearKml() ?: context?.let { it1 ->
                    LGConnectionDialog.show(it1) {

                    }
                }
            }
        }

        binding.mcvRelaunch.setOnClickListener {
            lifecycleScope.launch {
                lgService?.relaunch() ?: context?.let { it1 ->
                    LGConnectionDialog.show(it1) {

                    }
                }
            }
        }

        binding.mcvReboot.setOnClickListener {
            lifecycleScope.launch {
                lgService?.reboot() ?: context?.let { it1 ->
                    LGConnectionDialog.show(it1) {

                    }
                }
            }
        }

        binding.mcvPowerOff.setOnClickListener {
            lifecycleScope.launch {
                lgService?.powerOff() ?: context?.let { it1 ->
                    LGConnectionDialog.show(it1) {

                    }
                }
            }
        }

        return binding.root
    }
}