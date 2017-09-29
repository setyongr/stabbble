package com.dreamakasa.stabbble.ui.main

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.TransitionOptions

import com.dreamakasa.stabbble.R
import com.dreamakasa.stabbble.common.base.BaseInjectedActivity
import com.dreamakasa.stabbble.common.showDefaultError
import com.dreamakasa.stabbble.data.model.Pref
import com.dreamakasa.stabbble.data.model.User
import com.dreamakasa.stabbble.injection.component.ActivityComponent
import com.dreamakasa.stabbble.ui.splashscreen.SplashScreenActivity
import com.tapadoo.alerter.Alerter
import io.realm.Realm
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
                ListItem("New Followers", 0, 1),
                ListItem("Lost Followers", 0, -1),
                ListItem("Not Following Back", 0),
                ListItem("Everyone You Follow", 0),
                ListItem("Friends", 0),
                ListItem("Fans", 0)
        ))

        btn_refresh.setOnClickListener {
            presenter.sync()
        }

        btn_more.setOnClickListener{
            val popup = PopupMenu(this, btn_more)

            popup.menuInflater.inflate(R.menu.main_menu, popup.menu)

            popup.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.about_menu -> {
                        true
                    }
                    R.id.logout_menu -> {
                        pref.edit().remove(Pref.IS_LOGGED_ID).apply()
                        pref.edit().remove(Pref.SYNCED_FIRST).apply()
                        pref.edit().remove(Pref.ACCESS_TOKEN).apply()
                        Realm.deleteRealm(Realm.getDefaultConfiguration())
                        startActivity(Intent(this, SplashScreenActivity::class.java))
                        finish()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            popup.show()
        }
    }

    override fun onResume() {
        super.onResume()
        if(!pref.getBoolean(Pref.IS_LOGGED_ID, false)){
            startActivity(Intent(this, SplashScreenActivity::class.java))
            finish()
        }else{
            if(!pref.getBoolean(Pref.SYNCED_FIRST, false)) presenter.sync()
            else presenter.getAnayticsData()
        }

    }
    override fun injectModule(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun onSyncStart() {
        progres = ProgressDialog.show(this, "Loading...", "Analyzing your account...", true, false)
    }

    override fun onSyncComplete(result: AnalyticsResult) {
        (list.adapter as MainListAdapter).apply {
            listItem[0].value = result.new_follower.toInt()
            listItem[1].value = result.lost_follower.toInt()
            listItem[2].value = result.not_following_back.toInt()
            listItem[3].value = result.you_follow.toInt()
            listItem[4].value = result.friends.toInt()
            listItem[5].value = result.fans.toInt()
            notifyDataSetChanged()
        }
        progres?.dismiss()
        presenter.currentUser()
        pref.edit().putBoolean(Pref.SYNCED_FIRST, true).apply()
    }

    override fun onSyncError(title: String, content: String) {
        progres?.dismiss()
        Alerter.create(this).showDefaultError(title, content)
    }

    override fun showUserInfo(user: User) {
        text_username.text = user.username
        text_follower_count.text = user.followers_count.toString()
        text_following_count.text = user.followings_count.toString()
        Glide.with(this).load(user.avatar_url).into(circle_image_view)
    }
}
