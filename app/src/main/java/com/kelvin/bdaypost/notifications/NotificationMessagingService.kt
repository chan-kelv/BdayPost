package com.kelvin.bdaypost.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kelvin.bdaypost.util.SharedPrefsUtil
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@AndroidEntryPoint
class NotificationMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var sharedPrefsUtil: SharedPrefsUtil

    @Inject
    lateinit var notificationSender: NotificationSender

    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Timber.d("Refreshed token: $token")
        sharedPrefsUtil.putString(NOTIFICATION_KEY, token)
    }

    /**
     * Called when app is in foreground. If app is in background, notification goes directly
     * into system notifications tray.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.d("From: ${remoteMessage.from}")
        remoteMessage.notification?.let { notification ->
            val title: String? = notification.title
            val content: String? = notification.body
            if (!title.isNullOrBlank() && !content.isNullOrBlank())
                notificationSender.sendBirthdayNotification(title, content)
        }
    }

}