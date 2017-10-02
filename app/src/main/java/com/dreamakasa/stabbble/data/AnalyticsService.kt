package com.dreamakasa.stabbble.data

import android.util.Log
import com.dreamakasa.stabbble.data.model.*
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsService @Inject constructor(
        private val dataManager: DataManager
){
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun follower(onSuccess: ()->Unit, onError: (err: Throwable)->Unit){
        val realm = Realm.getDefaultInstance()

        realm.executeTransaction {
            val f = realm.where(Followers::class.java).findAll()
            for(i in f.size-1 downTo 0){
                f[i].flag = true
            }
        }

        realm.executeTransaction {
            it.where(NewFollower::class.java).findAll().deleteAllFromRealm()
            it.where(NewUnfollower::class.java).findAll().deleteAllFromRealm()
        }

        //When new data inserted as new row
        val f = realm.where(Followers::class.java).findAll()
        f.addChangeListener { _, changeSet ->
            if(changeSet.insertions.isNotEmpty()){
                changeSet.insertions.forEach {
                    i ->
                    realm.executeTransaction {
                        it.insertOrUpdate(NewFollower(
                                id = f[i].id,
                                name = f[i].name,
                                username = f[i].username,
                                bio = f[i].bio,
                                avatar_url = f[i].avatar_url))
                    }
                }
            }
        }

        compositeDisposable += dataManager.getFollower(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            data ->
                            //Add data to realm
                            realm.executeTransaction {
                                it.insertOrUpdate(Followers(
                                        id = data.follower.id,
                                        name = data.follower.name,
                                        username = data.follower.username,
                                        avatar_url = data.follower.avatar_url,
                                        bio = data.follower.bio,
                                        flag = false
                                ))
                            }
                        },
                        {
                            it.printStackTrace()
                            f.removeAllChangeListeners()
                            realm.close()
                            onError(it)
                        },
                        {
                            //For all that flag is true is not found in new data
                            realm.beginTransaction()
                            val unfollow = realm.where(Followers::class.java).equalTo("flag", true).findAll()
                            for (i in unfollow.size-1 downTo 0){
                                realm.insertOrUpdate(NewUnfollower(
                                        id = unfollow[i].id,
                                        name = unfollow[i].name,
                                        username = unfollow[i].username,
                                        bio = unfollow[i].bio,
                                        avatar_url = unfollow[i].avatar_url))
                            }
                            realm.commitTransaction()

                            //Remove old follower
                            realm.executeTransaction {
                                realm.where(Followers::class.java).equalTo("flag", true).findAll().deleteAllFromRealm()
                            }

                            f.removeAllChangeListeners()
                            realm.close()
                            onSuccess()
                        }
                )
    }

    fun following(onSuccess: () -> Unit, onError: (err: Throwable) -> Unit){
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val f = realm.where(Following::class.java).findAll()
            for(i in f.size-1 downTo 0){
                f[i].flag = true
            }
        }

        compositeDisposable += dataManager.getFollowing(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            data ->
                            //Add to follower
                            val followers = realm.where(Followers::class.java).equalTo("id", data.followee.id).findFirst()
                            val new_followers = realm.where(NewFollower::class.java).equalTo("id", data.followee.id).findFirst()
                            val new_unfollowers = realm.where(NewUnfollower::class.java).equalTo("id", data.followee.id).findFirst()
                            realm.executeTransaction {
                                it.insertOrUpdate(Following(
                                        id = data.followee.id,
                                        name = data.followee.name,
                                        username= data.followee.username,
                                        avatar_url = data.followee.avatar_url,
                                        bio = data.followee.bio,
                                        flag = false
                                ))

                                followers?.let {
                                    it.isFollowing = true
                                }
                                new_followers?.let {
                                    it.isFollowing = true
                                }
                                new_unfollowers?.let {
                                    it.isFollowing = true
                                }
                            }
                        },
                        {
                            it.printStackTrace()
                            realm.close()
                            onError(it)
                        },
                        {
                            //Remove old following
                            realm.executeTransaction {
                                realm.where(Following::class.java).equalTo("flag", true).findAll().deleteAllFromRealm()
                            }

                            val followingSet = realm.where(Following::class.java).findAll().map {
                                UserData(id = it.id)
                            }.toMutableSet()

                            val followerSet = realm.where(Followers::class.java).findAll().map {
                                UserData(id = it.id)
                            }.toMutableSet()

                            followerSet.forEach {
                                Log.d("Followers", it.toString())
                            }

                            followingSet.forEach {
                                Log.d("Following", it.toString())
                            }
                            val friendsSet = followerSet.intersect(followingSet)
                            val notFollowBackSet = followingSet.subtract(followerSet)
                            val fansSet = followerSet.subtract(followingSet)

                            realm.executeTransaction {
                                r ->
                                r.where(Friends::class.java).findAll().deleteAllFromRealm()
                                friendsSet.forEach {
                                    Log.d("Friends", it.toString())

                                    val u = r.where(Followers::class.java).equalTo("id", it.id).findFirst()
                                    u?.let {
                                        r.insert(Friends(
                                                id = u.id,
                                                name = u.name,
                                                username = u.username,
                                                avatar_url = u.avatar_url,
                                                bio = u.bio
                                        ))
                                    }
                                }

                                r.where(NotFollowingBack::class.java).findAll().deleteAllFromRealm()
                                notFollowBackSet.forEach {
                                    Log.d("Not Follow Back", it.toString())
                                    val u = r.where(Following::class.java).equalTo("id", it.id).findFirst()
                                    u?.let {
                                        r.insert(NotFollowingBack(
                                                id = u.id,
                                                name = u.name,
                                                username = u.username,
                                                avatar_url = u.avatar_url,
                                                bio = u.bio
                                        ))
                                    }
                                }

                                r.where(Fans::class.java).findAll().deleteAllFromRealm()
                                fansSet.forEach {
                                    Log.d("Fans", it.toString())

                                    val u = r.where(Followers::class.java).equalTo("id", it.id).findFirst()
                                    u?.let {
                                        r.insert(Fans(
                                                id = u.id,
                                                name = u.name,
                                                username = u.username,
                                                avatar_url = u.avatar_url,
                                                bio = u.bio
                                        ))
                                    }
                                }

                            }

                            realm.close()
                            onSuccess()
                        }
                )
    }


    fun syncFollowing(): Completable{
        fun initializeFlag(realm: Realm){
            realm.executeTransaction {
                val f = realm.where(Following::class.java).findAll()
                for(i in f.size-1 downTo 0){
                    f[i].flag = true
                }
            }
        }
        return Completable.create { emmiter ->
            val realm = Realm.getDefaultInstance()
            initializeFlag(realm)

            val disposable = dataManager.getFollowing(1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                data ->
                                //Add to follower
                                val followers = realm.where(Followers::class.java).equalTo("id", data.followee.id).findFirst()
                                val new_followers = realm.where(NewFollower::class.java).equalTo("id", data.followee.id).findFirst()
                                val new_unfollowers = realm.where(NewUnfollower::class.java).equalTo("id", data.followee.id).findFirst()
                                realm.executeTransaction {
                                    it.insertOrUpdate(Following(
                                            id = data.followee.id,
                                            name = data.followee.name,
                                            username= data.followee.username,
                                            avatar_url = data.followee.avatar_url,
                                            bio = data.followee.bio,
                                            flag = false
                                    ))

                                    followers?.let {
                                        it.isFollowing = true
                                    }
                                    new_followers?.let {
                                        it.isFollowing = true
                                    }
                                    new_unfollowers?.let {
                                        it.isFollowing = true
                                    }
                                }
                            },
                            {
                                it.printStackTrace()
                                realm.close()
                                emmiter.onError(it)
                            },
                            {
                                //Remove old following
                                realm.executeTransaction {
                                    realm.where(Following::class.java).equalTo("flag", true).findAll().deleteAllFromRealm()
                                }

                                val followingSet = realm.where(Following::class.java).findAll().map {
                                    UserData(id = it.id)
                                }.toMutableSet()

                                val followerSet = realm.where(Followers::class.java).findAll().map {
                                    UserData(id = it.id)
                                }.toMutableSet()

                                followerSet.forEach {
                                    Log.d("Followers", it.toString())
                                }

                                followingSet.forEach {
                                    Log.d("Following", it.toString())
                                }
                                val friendsSet = followerSet.intersect(followingSet)
                                val notFollowBackSet = followingSet.subtract(followerSet)
                                val fansSet = followerSet.subtract(followingSet)

                                realm.executeTransaction {
                                    r ->
                                    r.where(Friends::class.java).findAll().deleteAllFromRealm()
                                    friendsSet.forEach {
                                        Log.d("Friends", it.toString())

                                        val u = r.where(Followers::class.java).equalTo("id", it.id).findFirst()
                                        u?.let {
                                            r.insert(Friends(
                                                    id = u.id,
                                                    name = u.name,
                                                    username = u.username,
                                                    avatar_url = u.avatar_url,
                                                    bio = u.bio
                                            ))
                                        }
                                    }

                                    r.where(NotFollowingBack::class.java).findAll().deleteAllFromRealm()
                                    notFollowBackSet.forEach {
                                        Log.d("Not Follow Back", it.toString())
                                        val u = r.where(Following::class.java).equalTo("id", it.id).findFirst()
                                        u?.let {
                                            r.insert(NotFollowingBack(
                                                    id = u.id,
                                                    name = u.name,
                                                    username = u.username,
                                                    avatar_url = u.avatar_url,
                                                    bio = u.bio
                                            ))
                                        }
                                    }

                                    r.where(Fans::class.java).findAll().deleteAllFromRealm()
                                    fansSet.forEach {
                                        Log.d("Fans", it.toString())

                                        val u = r.where(Followers::class.java).equalTo("id", it.id).findFirst()
                                        u?.let {
                                            r.insert(Fans(
                                                    id = u.id,
                                                    name = u.name,
                                                    username = u.username,
                                                    avatar_url = u.avatar_url,
                                                    bio = u.bio
                                            ))
                                        }
                                    }

                                }

                                realm.close()
                                emmiter.onComplete()
                            }
                    )

            emmiter.setDisposable(Disposables.fromAction {
                if(!disposable.isDisposed){
                    disposable.dispose()
                    if(!realm.isClosed) realm.close()
                }
            })
        }
    }

    fun syncFollower(): Completable{
        return Completable.create { emmiter ->
            val realm = Realm.getDefaultInstance()

            realm.executeTransaction {
                val f = realm.where(Followers::class.java).findAll()
                for(i in f.size-1 downTo 0){
                    f[i].flag = true
                }
            }

            //Reset
            realm.executeTransaction {
                it.where(NewFollower::class.java).findAll().deleteAllFromRealm()
                it.where(NewUnfollower::class.java).findAll().deleteAllFromRealm()
            }

            //When new data inserted as new row
            val f = realm.where(Followers::class.java).findAll()
            f.addChangeListener { _, changeSet ->
                if(changeSet.insertions.isNotEmpty()){
                    changeSet.insertions.forEach {
                        i ->
                        realm.executeTransaction {
                            it.insertOrUpdate(NewFollower(
                                    id = f[i].id,
                                    name = f[i].name,
                                    username = f[i].username,
                                    bio = f[i].bio,
                                    avatar_url = f[i].avatar_url))
                        }
                    }
                }
            }

            val disposable = dataManager.getFollower(1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                data ->
                                //Add data to realm
                                realm.executeTransaction {
                                    it.insertOrUpdate(Followers(
                                            id = data.follower.id,
                                            name = data.follower.name,
                                            username = data.follower.username,
                                            avatar_url = data.follower.avatar_url,
                                            bio = data.follower.bio,
                                            flag = false
                                    ))
                                }
                            },
                            {
                                it.printStackTrace()
                                f.removeAllChangeListeners()
                                realm.close()
                                emmiter.onError(it)
                            },
                            {
                                //For all that flag is true is not found in new data
                                realm.beginTransaction()
                                val unfollow = realm.where(Followers::class.java).equalTo("flag", true).findAll()
                                for (i in unfollow.size-1 downTo 0){
                                    realm.insertOrUpdate(NewUnfollower(
                                            id = unfollow[i].id,
                                            name = unfollow[i].name,
                                            username = unfollow[i].username,
                                            bio = unfollow[i].bio,
                                            avatar_url = unfollow[i].avatar_url))
                                }
                                realm.commitTransaction()

                                //Remove old follower
                                realm.executeTransaction {
                                    realm.where(Followers::class.java).equalTo("flag", true).findAll().deleteAllFromRealm()
                                }

                                f.removeAllChangeListeners()
                                realm.close()
                                emmiter.onComplete()
                            }
                    )

            emmiter.setDisposable(Disposables.fromAction {
                if(!disposable.isDisposed){
                    disposable.dispose()
                    if(!realm.isClosed){
                        f.removeAllChangeListeners()
                        realm.close()
                    }
                }
            })
        }
    }
}