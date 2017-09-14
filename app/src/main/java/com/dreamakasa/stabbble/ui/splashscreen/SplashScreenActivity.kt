package com.dreamakasa.stabbble.ui.splashscreen

import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import com.dreamakasa.stabbble.R
import com.dreamakasa.stabbble.common.base.BaseInjectedActivity
import com.dreamakasa.stabbble.injection.component.ActivityComponent
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity: BaseInjectedActivity() {
    val authDialog = AuthDialog()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        btnLogin.setOnClickListener{
            if(authDialog.dialog?.isShowing != true){
                authDialog.show(supportFragmentManager, "auth")
            }
        }

        val anim = AnimationUtils.loadAnimation(this, R.anim.login_slide)
        btnLogin.startAnimation(anim)
    }


    override fun injectModule(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }


    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Splash", "OnDestroy")
    }

}