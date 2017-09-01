package com.dreamakasa.stabbble.injection.component

import com.dreamakasa.stabbble.injection.PerActivity
import com.dreamakasa.stabbble.ui.MainActivity
import com.dreamakasa.stabbble.injection.module.ActivityModule
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

}