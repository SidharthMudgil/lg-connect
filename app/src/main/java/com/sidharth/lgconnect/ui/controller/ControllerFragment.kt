package com.sidharth.lgconnect.ui.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.sidharth.lgconnect.R
import com.sidharth.lgconnect.databinding.FragmentControllerBinding
import com.sidharth.lgconnect.util.LGDialogs
import com.sidharth.lgconnect.util.LGManager
import com.sidharth.lgconnect.util.NetworkUtils
import com.sidharth.lgconnect.util.ResourceProvider
import com.sidharth.lgconnect.util.ToastUtils
import kotlinx.coroutines.launch


class ControllerFragment : Fragment() {
    private lateinit var resourceProvider: ResourceProvider
    private lateinit var binding: FragmentControllerBinding
    private var lgDialogs: LGDialogs? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resourceProvider = ResourceProvider(requireContext())
        lgDialogs = LGDialogs()
    }

    override fun onPause() {
        super.onPause()
        lgDialogs?.dismissNoConnectionDialog()
        lgDialogs = null
    }

    override fun onDestroy() {
        super.onDestroy()
        lgDialogs?.dismissNoConnectionDialog()
        lgDialogs = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentControllerBinding.inflate(inflater)

        binding.mcvSetSlaveRefresh.setOnClickListener {
            execute {
                lifecycleScope.launch {
                    LGManager.getInstance()?.setRefresh()
                }
            }
        }

        binding.mcvResetSlaveRefresh.setOnClickListener {
            execute {
                lifecycleScope.launch {
                    LGManager.getInstance()?.resetRefresh()
                }
            }
        }

        binding.mcvClearKml.setOnClickListener {
            execute {
                lifecycleScope.launch {
                    LGManager.getInstance()?.clearKml()
                }
            }
        }

        binding.mcvRelaunch.setOnClickListener {
            execute {
                lifecycleScope.launch {
                    LGManager.getInstance()?.relaunch()
                }
            }
        }

        binding.mcvReboot.setOnClickListener {
            execute {
                lifecycleScope.launch {
                    LGManager.getInstance()?.restart()
                }
            }
        }

        binding.mcvPowerOff.setOnClickListener {
            execute {
                lifecycleScope.launch {
                    LGManager.getInstance()?.shutdown()
                }
            }
        }

        return binding.root
    }

    private fun execute(action: () -> Unit) {
        if (NetworkUtils.isNetworkConnected(requireContext())) {
            if (LGManager.getInstance()?.connected == true) {
                action()
            } else {
                lgDialogs?.showNoConnectionDialog(requireContext())
            }
        } else {
            ToastUtils.showToast(requireContext(), "No Network Connection")
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