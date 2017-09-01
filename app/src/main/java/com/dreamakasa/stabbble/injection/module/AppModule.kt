package com.dreamakasa.stabbble.injection.module

import android.app.Application
import android.content.Context
import com.dreamakasa.stabbble.injection.ApplicationContext
import dagger.Module
import dagger.Provides

@Module
class AppModule(val app: Application) {

    @Provides
    fun provideApplication(): Application = app

    @Provides
    @ApplicationContext
    fun provideContext(): Context = app

}