package com.dreamakasa.stabbble.ui.main

import com.dreamakasa.stabbble.common.base.BasePresenter
import com.dreamakasa.stabbble.common.base.RxBasePresenter
import com.dreamakasa.stabbble.data.AnalyticsService
import com.dreamakasa.stabbble.data.DataManager
import com.dreamakasa.stabbble.data.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import javax.inject.Inject

class MainPresenter @Inject constructor(
        private val dataManager: DataManager,
        private val analyticsService: AnalyticsService
): RxBasePresenter<MainView>(){
    fun sync(){
        getView().onSyncStart()
        dataManager.syncUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            currentUser()
                            analyticsService.follower({
                                analyticsService.following({
                                    val realm = Realm.getDefaultInstance()
                                    getView().onSyncComplete(AnalyticsResult(
                                            new_follower = realm.where(NewFollower::class.java).count(),
                                            lost_follower = realm.where(NewUnfollower::class.java).count(),
                                            not_following_back = realm.where(NotFollowingBack::class.java).count(),
                                            you_follow = realm.where(Following::class.java).count(),
                                            friends = realm.where(Friends::class.java).count(),
                                            fans = realm.where(Fans::class.java).count()
                                    ))
                                    realm.close()
                                }, {
                                    getView().onSyncError("Whoopss", "Something Went Wrong")
                                })
                            },{
                                getView().onSyncError("Whoopss", "Something Went Wrong")

                            })
                        },
                        {
                            it.printStackTrace()
                            getView().onSyncError("Whoopss", "Something Went Wrong")
                        }
                )
    }

    fun getAnayticsData(){
        val realm = Realm.getDefaultInstance()
        getView().onSyncComplete(AnalyticsResult(
                new_follower = realm.where(NewFollower::class.java).count(),
                lost_follower = realm.where(NewUnfollower::class.java).count(),
                not_following_back = realm.where(NotFollowingBack::class.java).count(),
                you_follow = realm.where(Following::class.java).count(),
                friends = realm.where(Friends::class.java).count(),
                fans = realm.where(Fans::class.java).count()
        ))
        realm.close()
    }

    fun currentUser(){
        addDisposable(
                dataManager.currentUser()
                        .subscribe{
                            getView().showUserInfo(it)
                        }
        )

    }
}