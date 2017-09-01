package com.dreamakasa.stabbble.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.dreamakasa.stabbble.R
import com.dreamakasa.stabbble.common.base.BaseInjectedActivity
import com.dreamakasa.stabbble.injection.component.ActivityComponent

class MainActivity : BaseInjectedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun injectModule(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }
}
