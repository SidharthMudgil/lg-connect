package com.sidharth.lgconnect.util

import android.text.InputType
import com.google.android.material.textfield.TextInputEditText

object FormValidator {
    fun validate(fields: List<Pair<TextInputEditText, String>>): Boolean {
        var hasError = false
        fields.forEach { (field, errorMessage) ->
            val value = field.text.toString().trim()
            if (value.isEmpty()) {
                field.error = errorMessage
                hasError = true
            } else {
                field.error =
                    if (field.inputType == InputType.TYPE_CLASS_PHONE && !isValidIpAddress(value)) {
                        "Invalid IP address"
                    } else {
                        null
                    }
            }
        }
        return !hasError
    }

    private fun isValidIpAddress(ip: String): Boolean {
        val pattern = Regex(
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$"
        )
        return pattern.matches(ip)
    }
}