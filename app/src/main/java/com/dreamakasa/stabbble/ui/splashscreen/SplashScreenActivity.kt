package com.dreamakasa.stabbble.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import com.dreamakasa.stabbble.R
import com.dreamakasa.stabbble.common.base.BaseInjectedActivity
import com.dreamakasa.stabbble.injection.component.ActivityComponent
import com.dreamakasa.stabbble.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity: BaseInjectedActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        btnLogin.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
    override fun injectModule(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

}