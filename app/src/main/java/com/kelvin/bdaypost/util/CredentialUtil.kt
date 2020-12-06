package com.kelvin.bdaypost.util

import android.util.Patterns

object CredentialUtil {

    fun String?.isValidEmail() =
        !this.isNullOrBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}