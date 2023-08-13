package com.sidharth.lgconnect.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.sidharth.lgconnect.R
import com.sidharth.lgconnect.databinding.ActivityMainBinding
import com.sidharth.lgconnect.ui.codeeditor.CodeEditorFragment
import com.sidharth.lgconnect.ui.controller.ControllerFragment
import com.sidharth.lgconnect.ui.home.HomeFragment
import com.sidharth.lgconnect.ui.maps.MapsFragment
import com.sidharth.lgconnect.ui.settings.SettingsFragment
import com.sidharth.lgconnect.ui.viewmodel.ConnectionViewModel
import com.sidharth.lgconnect.util.LGManager
import com.sidharth.lgconnect.util.NetworkUtils.startNetworkCallback
import com.sidharth.lgconnect.util.NetworkUtils.stopNetworkCallback
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val connectionViewModel: ConnectionViewModel by viewModels()
    private var scheduledExecutorService: ScheduledExecutorService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setupBottomNavigation()
        startCallback()
    }

    override fun onPause() {
        super.onPause()
        stopNetworkCallback(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopNetworkCallback(this)
        scheduledExecutorService?.shutdown()
        scheduledExecutorService = null
    }

    private fun startCallback() {
        startNetworkCallback(
            context = this,
            onConnectionLost = {
                connectionViewModel.setConnectionStatus(false)
            },
            onConnectionAvailable = {

            }
        )

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
        scheduledExecutorService?.scheduleAtFixedRate({
            connectionViewModel.setConnectionStatus(LGManager.getInstance()?.connected == true)
        }, 0, 1, TimeUnit.SECONDS)
    }

    private fun setupBottomNavigation() {
        val homeFragment: Fragment = HomeFragment()
        val codeEditorFragment: Fragment = CodeEditorFragment()
        val mapsFragment: Fragment = MapsFragment()
        val controllerFragment: Fragment = ControllerFragment()
        val settingsFragment: Fragment = SettingsFragment()

        var activeFragment: Fragment? = null

        supportFragmentManager.beginTransaction().add(R.id.fragment_container, homeFragment, "home")
            .hide(homeFragment).add(R.id.fragment_container, codeEditorFragment, "code-editor")
            .hide(codeEditorFragment).add(R.id.fragment_container, mapsFragment, "maps")
            .hide(mapsFragment).add(R.id.fragment_container, controllerFragment, "controller")
            .hide(controllerFragment).add(R.id.fragment_container, settingsFragment, "settings")
            .hide(settingsFragment).commit()

        activeFragment = switchFragment(homeFragment, activeFragment)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    activeFragment = switchFragment(homeFragment, activeFragment)
                    true
                }

                R.id.code_editor -> {
                    activeFragment = switchFragment(codeEditorFragment, activeFragment)
                    true
                }

                R.id.maps -> {
                    activeFragment = switchFragment(mapsFragment, activeFragment)
                    true
                }

                R.id.controller -> {
                    activeFragment = switchFragment(controllerFragment, activeFragment)
                    true
                }

                R.id.settings -> {
                    activeFragment = switchFragment(settingsFragment, activeFragment)
                    true
                }

                else -> false
            }
        }
    }

    private fun switchFragment(fragment: Fragment, active: Fragment?): Fragment {
        if (active == fragment) return fragment

        if (active != null) {
            supportFragmentManager.beginTransaction().hide(active).show(fragment).commit()
        } else {
            supportFragmentManager.beginTransaction().show(fragment).commit()
        }

        return fragment
    }
}