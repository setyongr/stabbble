package com.dreamakasa.stabbble.injection.module

import android.app.Activity
import android.content.Context
import com.dreamakasa.stabbble.injection.ActivityContext
import com.dreamakasa.stabbble.injection.PerActivity
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(val activity: Activity) {

    @Provides
    @PerActivity
    fun provideActivity(): Activity = activity

    @Provides
    @ActivityContext
    fun provideActivityContext(): Context = activity
}