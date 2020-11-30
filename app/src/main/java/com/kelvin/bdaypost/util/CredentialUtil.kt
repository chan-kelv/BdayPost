package com.kelvin.bdaypost.util

import android.util.Patterns

class CredentialUtil {

    companion object {
        fun String?.isValidEmail() = !this.isNullOrBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}