package com.kelvin.bdaypost.data

import com.kelvin.bdaypost.data.model.LoggedInUser

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository private constructor(
    var dataSource: LoginDataSource = LoginDataSource()
) {
    suspend fun login(email:String, password: String): Result<LoggedInUser> {
        dataSource.getCurrentUser()?.let { fbuser ->
            return Result.Success(LoggedInUser(fbuser.uid, fbuser.displayName ?: "userDisplayName"))
        } ?: run {
            val authResult = dataSource.login(email, password)
            if (authResult is Result.Success) {
                authResult.data.user?.let {
                    return Result.Success(LoggedInUser(it.uid, it.displayName ?: "userDisplayName"))
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

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: LoginRepository().also { instance = it }
        }
    }

//    // in-memory cache of the loggedInUser object
//    var user: LoggedInUser? = null
//        private set
//
//    val isLoggedIn: Boolean
//        get() = user != null
//
//    init {
//        // If user credentials will be cached in local storage, it is recommended it be encrypted
//        // @see https://developer.android.com/training/articles/keystore
//        user = null
//    }
//
//    fun logout() {
//        user = null
//        dataSource.logout()
//    }
//
//    fun login(username: String, password: String): Result<LoggedInUser> {
//        // handle login
//        val result = dataSource.login(username, password)
//
//        if (result is Result.Success) {
//            setLoggedInUser(result.data)
//        }
//
//        return result
//    }
//
//    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
//        this.user = loggedInUser
//        // If user credentials will be cached in local storage, it is recommended it be encrypted
//        // @see https://developer.android.com/training/articles/keystore
//    }
}