package com.dreamakasa.stabbble.common.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dreamakasa.stabbble.BaseApp
import com.dreamakasa.stabbble.injection.component.ActivityComponent
import com.dreamakasa.stabbble.injection.module.ActivityModule

abstract class BaseInjectedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityComponent = BaseApp.get(this)
                .appComponent
                .activityComponent()
                .activityModule(ActivityModule(this))
                .build()

        injectModule(activityComponent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    abstract fun injectModule(activityComponent: ActivityComponent)
}