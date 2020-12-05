package com.kelvin.bdaypost.util

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
class UtilsModule {

    @Provides
    fun provideSharedPrefsUtil(@ApplicationContext context: Context): SharedPrefsUtil =
        SharedPrefsUtil(context)
}