package com.kelvin.bdaypost.data.loginData

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kelvin.bdaypost.data.Result
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.lang.Exception

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    private val fbAuth = FirebaseAuth.getInstance()

    suspend fun getCurrentUser(): FirebaseUser? = fbAuth.currentUser

    suspend fun login(email: String, password: String): Result<AuthResult> {
        return try {
            val authRes = fbAuth.signInWithEmailAndPassword(email, password).await()
            Result.Success(authRes)
        } catch (e: Exception) {
            Timber.e(e)
            Result.Error(e)
        }
    }
}