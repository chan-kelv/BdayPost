package com.kelvin.bdaypost.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kelvin.bdaypost.MainActivity

import com.kelvin.bdaypost.databinding.ActivityLoginBinding
import com.kelvin.bdaypost.ui.login.viewModel.LoginFormState
import com.kelvin.bdaypost.ui.login.viewModel.LoginResult
import com.kelvin.bdaypost.ui.login.viewModel.LoginViewModel
import com.kelvin.bdaypost.util.TextResUtil.Companion.getStringFromRes
import com.kelvin.bdaypost.util.TextResUtil.Companion.showToast

class LoginActivity : AppCompatActivity() {

    private lateinit var loginVM: LoginViewModel
    private lateinit var loginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val loginView = loginBinding.root
        setContentView(loginView)

        loginVM = ViewModelProvider(this, LoginViewModel.LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginVM.loginFormState.observe(this, Observer {
            observeLoginState(it)
        })

        loginVM.loginResult.observe(this, Observer {
            observeLoginResult(it)
        })

        loginBinding.bttnLogin.setOnClickListener { attemptLogin() }
    }

    private fun attemptLogin() {
        val email = loginBinding.inputEmail.text.toString()
        val password = loginBinding.inputPassword.text.toString()

        val isValidLoginFields = loginVM.validateLoginFormState(email, password)
        if (isValidLoginFields) {
            loginBinding.progressLoading.visibility = View.VISIBLE
            loginVM.login(email, password)
        }
    }

    private fun observeLoginState(loginState: LoginFormState?) {
        loginState?.let {
            loginState.usernameError?.let { emailErrorRes ->
                loginBinding.inputEmail.error = this.getStringFromRes(emailErrorRes)
            }
            loginState.passwordError?.let {pwErrorRes ->
                loginBinding.inputPassword.error = this.getStringFromRes(pwErrorRes)
            }
        }
    }

    private fun observeLoginResult(loginResult: LoginResult?) {
        loginResult?.let { validResult ->
            loginBinding.progressLoading.visibility = View.GONE

            validResult.error?.let { err ->
                this.showToast(err)
                loginBinding.inputPassword.setText("")
            }
            validResult.success?.let {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}