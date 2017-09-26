package com.dreamakasa.stabbble.ui.main

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast

import com.dreamakasa.stabbble.R
import com.dreamakasa.stabbble.common.base.BaseInjectedActivity
import com.dreamakasa.stabbble.common.showDefaultError
import com.dreamakasa.stabbble.data.model.Pref
import com.dreamakasa.stabbble.data.model.User
import com.dreamakasa.stabbble.injection.component.ActivityComponent
import com.dreamakasa.stabbble.ui.splashscreen.SplashScreenActivity
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseInjectedActivity(), MainView {

    @Inject lateinit var presenter: MainPresenter
    @Inject lateinit var pref: SharedPreferences

    var progres: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.attachView(this)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        list.itemAnimator = DefaultItemAnimator()
        list.layoutManager = LinearLayoutManager(this)
        list.addItemDecoration(DividerItemDecorator(this, LinearLayoutManager.VERTICAL))
        list.adapter = MainListAdapter(mutableListOf(
                ListItem("New Followers", 320, 1),
                ListItem("Lost Followers", 20, -1),
                ListItem("Not Following Back", 20),
                ListItem("Everyone You Follow", 5320),
                ListItem("Friends", 2090),
                ListItem("Fans", 3230)
        ))

        btn_refresh.setOnClickListener {
            presenter.sync()
        }
    }

    override fun onResume() {
        super.onResume()
        if(!pref.getBoolean(Pref.IS_LOGGED_ID, false)){
            startActivity(Intent(this, SplashScreenActivity::class.java))
            finish()
        }
    }
    override fun injectModule(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun onSyncStart() {
        progres = ProgressDialog.show(this, "Loading...", "Analyzing your account...", true, false)
    }

    override fun onSyncComplete(newFollowerCount: Long, newUnfollowerCount: Long) {
        Log.d("Follow", newFollowerCount.toString())
        Log.d("Follow", newUnfollowerCount.toString())
        (list.adapter as MainListAdapter).apply {
            listItem[0].value = newFollowerCount.toInt()
            listItem[1].value = newUnfollowerCount.toInt()
            notifyDataSetChanged()
        }
        progres?.dismiss()
        presenter.currentUser()
    }

    override fun onSyncError(title: String, content: String) {
        progres?.dismiss()
        Alerter.create(this).showDefaultError(title, content)
    }

    override fun showUserInfo(user: User) {
        text_username.text = user.username
        text_follower_count.text = user.followers_count.toString()
        text_following_count.text = user.followings_count.toString()
    }
}
