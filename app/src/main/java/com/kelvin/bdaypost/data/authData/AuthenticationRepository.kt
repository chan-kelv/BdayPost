package com.kelvin.bdaypost.data.authData

import com.kelvin.bdaypost.data.Result

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class AuthenticationRepository private constructor(
    var dataSource: AuthenticationNetwork = AuthenticationNetwork()
) {
    suspend fun login(email:String, password: String): Result<String> {
        dataSource.getCurrentUser()?.let { fbuser ->
            return Result.Success(fbuser.uid)
        } ?: run {
            val authResult = dataSource.login(email, password)
            if (authResult is Result.Success) {
                authResult.data.user?.let {
                    return Result.Success(it.uid)
                } ?: run {
                    return Result.Error(Exception("No user found"))
                }
            } else {
                return Result.Error((authResult as Result.Error).exception)
            }
        }
    }

    suspend fun register(email: String, password: String, displayName: String): Result<String> {
        val authResult = dataSource.register(email, password)
        if (authResult is Result.Success) {
            if (displayName.isNotBlank()) {
                dataSource.changeDisplayName(displayName)
            }
            return Result.Success(authResult.data.user?.uid ?: "") // should not be null anyway if success
        } else {
            return Result.Error((authResult as Result.Error).exception)
        }
    }

    companion object {
        @Volatile
        private var instance: AuthenticationRepository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: AuthenticationRepository().also {
                instance = it
            }
        }
    }
}