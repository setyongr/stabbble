package com.dreamakasa.stabbble.injection.component

import com.dreamakasa.stabbble.injection.PerActivity
import com.dreamakasa.stabbble.ui.main.MainActivity
import com.dreamakasa.stabbble.injection.module.ActivityModule
import com.dreamakasa.stabbble.ui.newfollower.NewFollowerActivity
import com.dreamakasa.stabbble.ui.splashscreen.AuthDialog
import com.dreamakasa.stabbble.ui.splashscreen.SplashScreenActivity
import dagger.Subcomponent


@PerActivity
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    @Subcomponent.Builder
    interface Builder {
        fun activityModule(activityModule: ActivityModule): Builder

        fun build(): ActivityComponent
    }

    fun inject(activity: MainActivity)
    fun inject(splashScreenActivity: SplashScreenActivity)
    fun inject(authDialog: AuthDialog)
    fun inject(newFollowerActivity: NewFollowerActivity)
}