package com.kelvin.bdaypost

import android.app.Application
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BirthdayPostApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        logFirebaseMessagingToken()
    }

    /**
     * Log Firebase Messaging token to logcat for notification testing.
     */
    private fun logFirebaseMessagingToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.w("Fetching FCM registration token failed with exception ${task.exception}")
            } else {
                Timber.d("Got Firebase Messaging registration token: ${task.result}")
            }
        }
    }
}