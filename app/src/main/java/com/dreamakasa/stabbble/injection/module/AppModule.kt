package com.dreamakasa.stabbble.injection.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.dreamakasa.stabbble.data.local.LocalStabbbleService
import com.dreamakasa.stabbble.injection.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: Application) {

    @Provides
    fun provideApplication(): Application = app

    @Provides
    @ApplicationContext
    fun provideContext(): Context = app

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context) = PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @Singleton
    fun provideLocalStabbble(): LocalStabbbleService = LocalStabbbleService()
}