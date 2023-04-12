package com.sidharth.lgconnect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_LGConnect)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)

        val homeFragment: Fragment = HomeFragment()
        val mapsFragment: Fragment = MapsFragment()
        val controllerFragment: Fragment = ControllerFragment()

        val fragmentManager: FragmentManager = supportFragmentManager

        fragmentManager.beginTransaction()
            .add(R.id.fragment_container, homeFragment, "home-fragment")
            .commit()

        fragmentManager.beginTransaction()
            .add(R.id.fragment_container, controllerFragment, "controller-fragment")
            .hide(controllerFragment)
            .commit()

        fragmentManager.beginTransaction()
            .add(R.id.fragment_container, mapsFragment, "maps-fragment")
            .hide(mapsFragment)
            .commit()

        var active: Fragment = homeFragment

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    fragmentManager.beginTransaction()
                        .hide(active).show(homeFragment).commit()
                    active = homeFragment
                    true
                }
                R.id.maps -> {
                    fragmentManager.beginTransaction()
                        .hide(active).show(mapsFragment).commit()
                    active = mapsFragment
                    true
                }
                R.id.controller -> {
                    fragmentManager.beginTransaction()
                        .hide(active).show(controllerFragment).commit()
                    active = controllerFragment
                    true
                }
                else -> false
            }
        }
    }
}

