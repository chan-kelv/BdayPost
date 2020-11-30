package com.kelvin.bdaypost.ui.login.viewModel

/**
 * Authentication result : success (firebase uuid) or error message.
 */
data class LoginResult(
    val success: String? = null,
    val error: Int? = null
)