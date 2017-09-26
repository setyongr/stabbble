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
    var looperScheduler: Scheduler? = null
    var writeScheduler: Scheduler? = null
    init {
        val handlerThread = HandlerThread("LOOPER_SCHEDULER")
        handlerThread.start()
        synchronized(handlerThread) {
            looperScheduler = AndroidSchedulers.from(handlerThread.looper)
        }

        // write scheduler
        writeScheduler = Schedulers.from(Executors.newSingleThreadExecutor())
    }

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

    fun getFollower(page: Int): Observable<List<FollowerRes>>{
        return remote.getFollowers(page, 100)
                .concatMap {
                    if(it.isEmpty()){
                        Observable.just(it)
                    }else{
                        Observable.just(it).concatWith(getFollower(page+1))
                    }
                }
    }

    fun getFollowing(page: Int): Observable<List<FollowingRes>>{
        return remote.getFollowing(page, 100)
                .concatMap {
                    if(it.isEmpty()){
                        Observable.just(it)
                    }else{
                        Observable.just(it).concatWith(getFollowing(page+1))
                    }
                }
    }

    fun syncNew(): Completable{
        return Completable.create{ emmiter ->
            val realm = Realm.getDefaultInstance()
            realm.executeTransaction {
                val f = realm.where(Followers::class.java).findAll()
                for(i in f.size-1 downTo 0){
                    f[i].is_analisys = true
                }
            }
            realm.executeTransaction {
                it.where(NewFollower::class.java).findAll().deleteAllFromRealm()
                it.where(NewUnfollower::class.java).findAll().deleteAllFromRealm()
            }

            val follower = realm.where(Followers::class.java).findAll()
            follower.addChangeListener { t, changeSet ->
                if(changeSet.insertions.isNotEmpty()){
                    changeSet.insertions.forEach {
                        i ->
                        realm.executeTransaction {
                            it.insertOrUpdate(NewFollower(
                                    id = follower[i].id,
                                    name = follower[i].name,
                                    username = follower[i].username,
                                    bio = follower[i].bio,
                                    avatar_url = follower[i].avatar_url))
                        }
                    }
                }
            }

            val d = getFollower(1)
                    .subscribe (
                            {
                                it.forEach {
                                    data ->
                                    realm.executeTransaction {
                                        it.insertOrUpdate(Followers(
                                                id = data.follower.id,
                                                name = data.follower.name,
                                                username = data.follower.username,
                                                avatar_url = data.follower.avatar_url,
                                                bio = data.follower.bio,
                                                is_analisys = false
                                        ))
                                    }
                                }
                            },
                            {
                                follower.removeAllChangeListeners()
                                realm.close()
                                emmiter.onError(it)
                            },
                            {
                                realm.beginTransaction()
                                val unfollow = realm.where(Followers::class.java).equalTo("is_analisys", true).findAll()
                                for (i in unfollow.size-1 downTo 0){

                                    realm.insertOrUpdate(NewUnfollower(
                                            id = unfollow[i].id,
                                            name = unfollow[i].name,
                                            username = unfollow[i].username,
                                            bio = unfollow[i].bio,
                                            avatar_url = unfollow[i].avatar_url))
                                }
                                realm.commitTransaction()
                                realm.executeTransaction {
                                    realm.where(Followers::class.java).equalTo("is_analisys", true).findAll().deleteAllFromRealm()
                                }
                                follower.removeAllChangeListeners()
                                realm.close()
                                emmiter.onComplete()
                            }
                    )
            emmiter.setDisposable(Disposables.fromAction {
                if(follower.isValid){
                    follower.removeAllChangeListeners()
                }
                d.dispose()
                realm.close()
            })
        }.subscribeOn(looperScheduler)
    }

    fun syncFollowing(): Completable{
        return Completable.create { emitter ->
            val realm = Realm.getDefaultInstance()
            realm.executeTransaction {
                it.where(Following::class.java).findAll().deleteAllFromRealm()
            }

            val disposable = getFollowing(1)
                    .subscribe(
                            {
                                it.forEach {
                                    realm.insert(Following(
                                            id = it.id,
                                            name = it.followee.name
                                    ))
                                }
                            },
                            {
                                it.printStackTrace()
                                realm.close()
                                emitter.onError(it)
                            },
                            {
                                val follower = realm.where(Followers::class.java)
                                        .findAll()
                                        .map {
                                            User(
                                                    id = it.id,
                                                    name = it.name,
                                                    username = it.username,
                                                    avatar_url = it.avatar_url
                                            )
                                        }
                                        .toMutableSet()
                                val following = realm.where(Following::class.java)
                                        .findAll()
                                        .map {
                                            User(
                                                    id = it.id,
                                                    name = it.name,
                                                    username = it.username,
                                                    avatar_url = it.avatar_url
                                            )
                                        }
                                        .toMutableSet()


                                val notFollowback = following.subtract(follower)
                                notFollowback.forEach {
                                    user ->
                                    realm.executeTransaction {
                                        it.insertOrUpdate(NotFollowingBack(
                                                id = user.id,
                                                name = user.name,
                                                username = user.username,
                                                bio = user.bio,
                                                avatar_url = user.avatar_url
                                        ))
                                    }
                                }

                                realm.close()
                                emitter.onComplete()
                            }
                    )
            emitter.setDisposable(Disposables.fromAction {
                if(!disposable.isDisposed) disposable.dispose()
                realm.close()
            })

        }.subscribeOn(looperScheduler)
    }

    fun currentUser(): Observable<User> = local.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}