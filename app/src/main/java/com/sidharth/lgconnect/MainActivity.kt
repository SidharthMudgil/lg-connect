package com.sidharth.lgconnect

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.sidharth.lgconnect.databinding.ActivityMainBinding
import com.sidharth.lgconnect.ui.codeeditor.CodeEditorFragment
import com.sidharth.lgconnect.ui.controller.ControllerFragment
import com.sidharth.lgconnect.ui.home.HomeFragment
import com.sidharth.lgconnect.ui.maps.MapsFragment
import com.sidharth.lgconnect.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

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

