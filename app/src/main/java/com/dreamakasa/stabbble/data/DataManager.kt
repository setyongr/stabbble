package com.dreamakasa.stabbble.data

import android.os.HandlerThread
import com.dreamakasa.stabbble.data.local.LocalStabbbleService
import com.dreamakasa.stabbble.data.model.*
import com.dreamakasa.stabbble.data.remote.StabbbleService
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManager @Inject constructor(
        private val remote: StabbbleService,
        private val local: LocalStabbbleService
){

    private val realm: Realm = Realm.getDefaultInstance()

    fun syncUser(): Observable<User>{
        return remote.getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap {
                    local.setCurrentUser(User(
                            id = it.id,
                            name = it.name,
                            username = it.username,
                            avatar_url = it.avatar_url,
                            html_url = it.html_url,
                            followers_count = it.followers_count,
                            followings_count = it.followings_count
                    ))
                }
    }

    fun getFollower(page: Int) = remote.getFollowers(page, 100)
            .concatMap {
                if(it.isEmpty()){
                    Observable.just(it)
                }else{
                    Observable.just(it).concatWith(remote.getFollowers(page+1, 100))
                }
            }
            .flatMap {
                Observable.fromIterable(it)
            }

    fun getFollowing(page: Int) = remote.getFollowing(page, 100)
            .concatMap {
                if(it.isEmpty()){
                    Observable.just(it)
                }else{
                    Observable.just(it).concatWith(remote.getFollowing(page+1, 100))
                }
            }
            .flatMap {
                Observable.fromIterable(it)
            }

    fun currentUser(): Observable<User> = local.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getLocalNewFollower(search: String = "") = realm.where(NewFollower::class.java).like("name", "*$search*").findAllSortedAsync("name")
    fun getLocalNewUnfollower(search: String = "") = realm.where(NewUnfollower::class.java).like("name", "*$search*").findAllSortedAsync("name")
    fun getLocalNotFollowingBack(search: String = "") = realm.where(NotFollowingBack::class.java).like("name", "*$search*").findAllSortedAsync("name")
    fun getLocalFollower(search: String = "") = realm.where(Followers::class.java).like("name", "*$search*").findAllSortedAsync("name")
    fun getLocalFollowing(search: String = "") = realm.where(Following::class.java).like("name", "*$search*").findAllSortedAsync("name")
    fun getLocalFriends(search: String = "") = realm.where(Friends::class.java).like("name", "*$search*").findAllSortedAsync("name")
    fun getLocalFans(search: String = "") = realm.where(Fans::class.java).like("name", "*$search*").findAllSortedAsync("name")
}