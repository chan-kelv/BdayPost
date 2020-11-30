package com.kelvin.bdaypost.data.loginData

import com.kelvin.bdaypost.data.Result

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository private constructor(
    var dataSource: LoginDataSource = LoginDataSource()
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
    companion object {
        @Volatile
        private var instance: LoginRepository? = null

        fun getInstance() = instance
            ?: synchronized(this) {
            instance
                ?: LoginRepository()
                    .also { instance = it }
        }
    }
}