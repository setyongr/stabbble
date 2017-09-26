package com.dreamakasa.stabbble.ui.main

import com.dreamakasa.stabbble.common.base.BasePresenter
import com.dreamakasa.stabbble.common.base.RxBasePresenter
import com.dreamakasa.stabbble.data.DataManager
import com.dreamakasa.stabbble.data.model.NewFollower
import com.dreamakasa.stabbble.data.model.NewUnfollower
import com.dreamakasa.stabbble.data.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import javax.inject.Inject

class MainPresenter @Inject constructor(
        private val dataManager: DataManager
): RxBasePresenter<MainView>(){
    fun sync(){
        getView().onSyncStart()
        addDisposable(
                dataManager.syncUser()
                        .subscribe(
                                {
                                    currentUser()
                                    syncFollower()
                                },
                                {
                                    it.printStackTrace()
                                    getView().onSyncError("Whoopss", "Something Went Wrong")
                                }
                        )
        )
    }

    fun syncFollower(){
        addDisposable(
                dataManager.syncNew()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    val realm = Realm.getDefaultInstance()

                                    getView().onSyncComplete(realm.where(NewFollower::class.java).count(), realm.where(NewUnfollower::class.java).count())
                                },
                                {
                                    it.printStackTrace()
                                    getView().onSyncError("Whoopss", "Something Went Wrong")
                                }
                        )
        )
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