package com.sidharth.lgconnect.ui.controller

import com.sidharth.lgconnect.service.LGService
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.sidharth.lgconnect.databinding.FragmentControllerBinding
import com.sidharth.lgconnect.service.ServiceManager
import com.sidharth.lgconnect.util.ResourceProvider
import kotlinx.coroutines.launch


class ControllerFragment : Fragment() {
    private lateinit var resourceProvider: ResourceProvider
    private var lgService: LGService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resourceProvider = ResourceProvider(requireContext())
        if (ServiceManager.getSSHService()?.isConnected == true) {
            lgService = ServiceManager.getLGService()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentControllerBinding.inflate(inflater)

//        binding.tvConnectionStatus.text =
//        binding.tvConnectionStatus.setTextColor()

        binding.mcvSetSlaveRefresh.setOnClickListener {
            lifecycleScope.launch {
                lgService?.setRefresh() ?: doSomething()
            }
        }

        binding.mcvSetSlaveRefresh.setOnClickListener {
            lifecycleScope.launch {
                lgService?.setRefresh() ?: doSomething()
            }
        }

        binding.mcvResetSlaveRefresh.setOnClickListener {
            lifecycleScope.launch {
                lgService?.resetRefresh() ?: doSomething()
            }
        }

        binding.mcvClearKml.setOnClickListener {
            lifecycleScope.launch {
                lgService?.clearKml() ?: doSomething()
            }
        }

        binding.mcvRelaunch.setOnClickListener {
            lifecycleScope.launch {
                lgService?.relaunch() ?: doSomething()
            }
        }

        binding.mcvReboot.setOnClickListener {
            lifecycleScope.launch {
                lgService?.reboot() ?: doSomething()
            }
        }

        binding.mcvPowerOff.setOnClickListener {
            lifecycleScope.launch {
                lgService?.powerOff() ?: doSomething()
            }
        }

        return binding.root
    }

    fun doSomething() {}
}