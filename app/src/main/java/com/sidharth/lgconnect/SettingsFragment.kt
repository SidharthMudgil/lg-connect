package com.sidharth.lgconnect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.sidharth.lgconnect.databinding.FragmentSettingsBinding

const val HINT_USERNAME: String = "username"
const val HINT_PASSWORD: String = "password"
const val HINT_IP: String = "192.168.1.14"
const val HINT_PORT: String = "8080"

class SettingsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bundle = FragmentSettingsBinding.inflate(inflater)

        bundle.etUsername.setOnFocusChangeListener { v, hasFocus ->
            (v as EditText).hint = if (hasFocus) HINT_USERNAME else ""
        }

        bundle.etPassword.setOnFocusChangeListener { v, hasFocus ->
            (v as EditText).hint = if (hasFocus) HINT_PASSWORD else ""
        }

        bundle.etIp.setOnFocusChangeListener { v, hasFocus ->
            (v as EditText).hint = if (hasFocus) HINT_IP else ""
        }

        bundle.etPort.setOnFocusChangeListener { v, hasFocus ->
            (v as EditText).hint = if (hasFocus) HINT_PORT else ""
        }

        return bundle.root
    }

}