package com.dreamakasa.stabbble.ui.newfollower

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.dreamakasa.stabbble.R
import com.dreamakasa.stabbble.common.base.BaseInjectedActivity
import com.dreamakasa.stabbble.data.model.NewFollower
import com.dreamakasa.stabbble.injection.component.ActivityComponent
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_new_follower.*

class NewFollowerActivity: BaseInjectedActivity(){

    val realm: Realm = Realm.getDefaultInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_follower)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        list.layoutManager = LinearLayoutManager(this)
        list.adapter = NewFollowerAdapter(realm.where(NewFollower::class.java).findAll())

    }
    override fun injectModule(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

}