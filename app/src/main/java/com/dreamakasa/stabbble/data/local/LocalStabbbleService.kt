package com.dreamakasa.stabbble.data.local

import android.os.HandlerThread
import com.dreamakasa.stabbble.data.model.FollowerRes
import com.dreamakasa.stabbble.data.model.Followers
import com.dreamakasa.stabbble.data.model.Following
import com.dreamakasa.stabbble.data.model.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.realm.Realm
import io.realm.RealmChangeListener
import io.reactivex.internal.disposables.DisposableHelper.isDisposed
import io.reactivex.schedulers.Schedulers
import io.realm.RealmResults
import java.util.concurrent.Executors


class LocalStabbbleService{
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

    fun getCurrentUser(): Observable<User>{
        return Observable.create<User> { emitter ->
            val realm = Realm.getDefaultInstance()
            val result = realm.where(User::class.java).findFirstAsync()

            val realmChangeListener:RealmChangeListener<User> = RealmChangeListener {
                if(it.isLoaded && !emitter.isDisposed) emitter.onNext(User(
                        id = it.id,
                        name = it.name,
                        username = it.username,
                        avatar_url = it.avatar_url,
                        followers_count = it.followers_count,
                        followings_count = it.followings_count
                ))
                emitter.onComplete()
            }

            result.addChangeListener(realmChangeListener)
            emitter.setDisposable(Disposables.fromAction {
                if(result.isValid){
                    result.removeAllChangeListeners()
                }
                realm.close()
            })
        }
                .subscribeOn(looperScheduler)
                .unsubscribeOn(looperScheduler)
    }

    fun setCurrentUser(user: User): Observable<User>{
        return Observable.create<User> ({ emitter ->
            Realm.getDefaultInstance().use { // <-- auto-close
                it.executeTransaction { realm -> realm.insertOrUpdate(user) }
                emitter.onNext(User(
                        id = user.id,
                        name = user.name,
                        username = user.username,
                        avatar_url = user.avatar_url,
                        followers_count = user.followers_count,
                        followings_count = user.followings_count
                ))
                emitter.onComplete()
            }
        }).subscribeOn(writeScheduler)
    }




}