package com.kelvin.bdaypost.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefsUtil @Inject constructor(@ApplicationContext private val context: Context) {

    fun putString(key: String, value: String) {
        context
            .getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
            .edit()
            .putString(key, value)
            .apply()
    }

    companion object {
        const val SHARED_PREF_KEY = "bday_post_shared_pref"
    }
}