package com.dreamakasa.stabbble.ui.list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.dreamakasa.stabbble.R
import com.dreamakasa.stabbble.common.base.BaseInjectedActivity
import com.dreamakasa.stabbble.data.DataManager
import com.dreamakasa.stabbble.data.model.*
import com.dreamakasa.stabbble.injection.component.ActivityComponent
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_new_follower.*
import javax.inject.Inject

class ListActivity : BaseInjectedActivity(){

    @Inject lateinit var dataManager: DataManager
    companion object {
        val NEW_FOLLOWER = 1
        val LOST_FOLLOWER = 2
        val FANS = 3
        val FRIENDS = 4
        val NOT_FOLLOWING_BACK = 5
        val FOLLOWERS = 6
        val FOLLOWING = 7
    }

    var items: RealmResults<out RealmObject>? = null
    val realm: Realm = Realm.getDefaultInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_follower)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        list.layoutManager = LinearLayoutManager(this)

        items = getObjects()

        @Suppress("UNCHECKED_CAST")
        list.adapter = when (intent.getIntExtra("page", 0)) {
            NEW_FOLLOWER -> ListAdapter<NewFollower>(items as RealmResults<NewFollower>)
            LOST_FOLLOWER -> ListAdapter<NewUnfollower>(items as RealmResults<NewUnfollower>)
            FANS -> ListAdapter<Fans>(items as RealmResults<Fans>)
            FRIENDS -> ListAdapter<Friends>(items as RealmResults<Friends>)
            NOT_FOLLOWING_BACK -> ListAdapter<NotFollowingBack>(items as RealmResults<NotFollowingBack>)
            FOLLOWERS -> ListAdapter<Followers>(items as RealmResults<Followers>)
            FOLLOWING -> ListAdapter<Following>(items as RealmResults<Following>)
            else -> null
        }
    }

    override fun injectModule(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    fun getObjects(search: String = "") = when (intent.getIntExtra("page", 0)) {
        NEW_FOLLOWER -> dataManager.getLocalNewFollower(search)
        LOST_FOLLOWER -> dataManager.getLocalNewUnfollower(search)
        FANS -> dataManager.getLocalFans(search)
        FRIENDS -> dataManager.getLocalFriends(search)
        NOT_FOLLOWING_BACK -> dataManager.getLocalNotFollowingBack(search)
        FOLLOWERS -> dataManager.getLocalFollower(search)
        FOLLOWING -> dataManager.getLocalFollowing(search)
        else -> null
    }

}