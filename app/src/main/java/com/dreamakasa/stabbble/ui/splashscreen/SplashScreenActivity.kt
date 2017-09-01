package com.dreamakasa.stabbble.ui.splashscreen

import android.os.Bundle
import com.dreamakasa.stabbble.R
import com.dreamakasa.stabbble.common.base.BaseInjectedActivity
import com.dreamakasa.stabbble.injection.component.ActivityComponent

class SplashScreenActivity: BaseInjectedActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }
    override fun injectModule(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

}