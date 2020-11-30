package com.kelvin.bdaypost.ui.login

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kelvin.bdaypost.data.LoginRepository

import com.kelvin.bdaypost.databinding.ActivityLoginBinding
import com.kelvin.bdaypost.util.TextResUtil.Companion.getStringFromRes
import kotlinx.coroutines.coroutineScope

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loginViewBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewBinding = ActivityLoginBinding.inflate(layoutInflater)
        val loginView = loginViewBinding.root
        setContentView(loginView)

        loginViewModel = ViewModelProvider(this, LoginViewModel.LoginViewModelFactory(
            LoginRepository.getInstance()))
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this, Observer {
            observeLoginState(it)
        })

        loginViewModel.loginResult.observe(this, Observer {
            observeLoginResult(it)
        })

        loginViewBinding.bttnLogin.setOnClickListener { attemptLogin() }
    }

    private fun attemptLogin() {
        val email = loginViewBinding.inputEmail.text.toString()
        val password = loginViewBinding.inputPassword.text.toString()
        val loginState = loginViewModel.validateLoginFormState(email, password)

        if (!loginState.isDataValid) {
            loginState.usernameError?.let { emailErrorRes ->
                loginViewBinding.inputEmail.error = this.getStringFromRes(emailErrorRes)
            }
            loginState.passwordError?.let {pwErrorRes ->
                loginViewBinding.inputPassword.error = this.getStringFromRes(pwErrorRes)
            }
        } else {
            loginViewModel.login(email, password)
        }
    }

    private fun observeLoginState(loginState: LoginFormState?) {
        loginState?.let {

        }
    }

    private fun observeLoginResult(loginResult: LoginResult?) {
        loginResult?.let { validResult ->
            loginViewBinding.progressLoading.visibility = View.GONE

            validResult.error?.let { err ->

            }
            validResult.success?.let {

            }
        }
    }
}