package com.kelvin.bdaypost.notifications

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
class NotificationsModule {

    @Provides
    fun provideNotificationSender(@ApplicationContext context: Context): NotificationSender =
        NotificationSender(context)
}