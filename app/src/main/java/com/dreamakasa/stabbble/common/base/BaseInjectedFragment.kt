package com.dreamakasa.stabbble.common.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dreamakasa.stabbble.BaseApp
import com.dreamakasa.stabbble.injection.component.ActivityComponent
import com.dreamakasa.stabbble.injection.module.ActivityModule

abstract class BaseInjectedFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityComponent = BaseApp.get(activity)
                .appComponent
                .activityComponent()
                .activityModule(ActivityModule(activity))
                .build()

        injectModule(activityComponent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayout(), container, false)
    }

    abstract fun getLayout(): Int

    abstract fun injectModule(activityComponent: ActivityComponent)
}