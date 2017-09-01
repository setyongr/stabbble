package com.dreamakasa.stabbble.injection.component

import com.dreamakasa.stabbble.injection.module.NetworkModule
import com.dreamakasa.stabbble.injection.module.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AppModule::class,
        NetworkModule::class
))
interface AppComponent {
    fun activityComponent(): ActivityComponent.Builder
}