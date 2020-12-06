package com.kelvin.bdaypost.login.viewModel

import androidx.lifecycle.*
import com.kelvin.bdaypost.R
import com.kelvin.bdaypost.login.data.AuthenticationRepository
import com.kelvin.bdaypost.data.Result
import com.kelvin.bdaypost.util.CredentialUtil.isValidEmail
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val authRepo: AuthenticationRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    /**
     * Validates the login input. Email must be in valid email format, password must not be null.
     * Updates loginFormState if error is found.
     * @return dataIsValid: Boolean - if the fields are valid to attempt login
     */
    fun validateLoginFormState(email: String, password: String): Boolean {
        val loginState =
            if (!email.isValidEmail()) {
                LoginFormState(
                    usernameError = R.string.invalid_username
                )
            } else if (password.isBlank()) {
                LoginFormState(
                    passwordError = R.string.invalid_password
                )
            } else {
                LoginFormState(isDataValid = true)
            }
        _loginForm.value = loginState
        return loginState.isDataValid
    }

    /**
     * Assumes valid email and password and will auth with firebase
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            withContext(IO) {
                val result = authRepo.login(email, password)
                withContext(Main) {
                    if (result is Result.Success) {
                        _loginResult.value = LoginResult( success = result.data)
                    } else {
                        _loginResult.value = LoginResult( error = R.string.login_failed)
                    }
                }
            }
        }
    }

    fun register(email: String, password: String, displayName: String) {
        viewModelScope.launch {
            withContext(IO) {
                val result = authRepo.register(email, password, displayName)
                withContext(Main) {
                    if (result is Result.Success) {
                        _loginResult.value = LoginResult(success = result.data)
                    } else {
                        _loginResult.value = LoginResult( error = R.string.login_failed)
                    }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class LoginViewModelFactory(private val authRepo: AuthenticationRepository = AuthenticationRepository.getInstance()) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LoginViewModel(
                authRepo
            ) as T
        }
    }
}