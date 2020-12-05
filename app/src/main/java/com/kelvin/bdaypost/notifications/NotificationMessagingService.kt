package com.kelvin.bdaypost.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kelvin.bdaypost.util.SharedPrefsUtil
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

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

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.d("From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            val title: String? = remoteMessage.data[NOTIFICATION_TITLE]
            val content: String? = remoteMessage.data[NOTIFICATION_CONTENT]
            if (!title.isNullOrBlank() && !content.isNullOrBlank())
                notificationSender.sendBirthdayNotification(title, content)
        }
    }
}