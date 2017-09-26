package com.dreamakasa.stabbble

import android.app.Application
import android.content.Context
import com.dreamakasa.stabbble.injection.component.AppComponent
import com.dreamakasa.stabbble.injection.component.DaggerAppComponent
import com.dreamakasa.stabbble.injection.module.AppModule
import com.dreamakasa.stabbble.injection.module.NetworkModule
import io.realm.Realm

class BaseApp : Application() {
    companion object {
        fun get(context: Context): BaseApp = context.applicationContext as BaseApp
    }

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .networkModule(NetworkModule())
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}