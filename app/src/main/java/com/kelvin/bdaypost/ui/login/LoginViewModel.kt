package com.kelvin.bdaypost.ui.login

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.kelvin.bdaypost.R
import com.kelvin.bdaypost.data.LoginRepository
import com.kelvin.bdaypost.data.Result
import com.kelvin.bdaypost.util.CredentialUtil.Companion.isValidEmail
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val loginRepo: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val firebaseAuth = FirebaseAuth.getInstance()

    fun validateLoginFormState(email: String, password: String): LoginFormState {
        return if (!email.isValidEmail()) {
            LoginFormState(usernameError = R.string.invalid_username)
        } else if (password.isBlank()) {
            LoginFormState(passwordError = R.string.invalid_password)
        } else {
            LoginFormState(isDataValid = true)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            withContext(IO) {
                val result = loginRepo.login(email, password)
                withContext(Main) {
                    if (result is Result.Success) {
                        _loginResult.value =
                            LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
                    } else {
                        _loginResult.value = LoginResult(error = R.string.login_failed)
                    }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class LoginViewModelFactory(private val loginRepo: LoginRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LoginViewModel(loginRepo) as T
        }
    }

//    fun login(username: String, password: String) {
//        // can be launched in a separate asynchronous job
//        val result = loginRepository.login(username, password)
//
//        if (result is Result.Success) {
//            _loginResult.value =
//                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
//        } else {
//            _loginResult.value = LoginResult(error = R.string.login_failed)
//        }
//    }
//
//    fun loginDataChanged(username: String, password: String) {
//        if (!isUserNameValid(username)) {
//            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
//        } else if (!isPasswordValid(password)) {
//            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
//        } else {
//            _loginForm.value = LoginFormState(isDataValid = true)
//        }
//    }
//
//    // A placeholder username validation check
//    private fun isUserNameValid(username: String): Boolean {
//        return if (username.contains('@')) {
//            Patterns.EMAIL_ADDRESS.matcher(username).matches()
//        } else {
//            username.isNotBlank()
//        }
//    }
//
//    // A placeholder password validation check
//    private fun isPasswordValid(password: String): Boolean {
//        return password.length > 5
//    }
}