package com.dreamakasa.stabbble.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

data class UserData(
        val id: Int?
)

data class PlainUser(
        var id: Int? = null,
        var name: String? = null,
        var username: String? = null,
        var avatar_url: String? = null,
        var bio: String? = null,
        var isFollowing: Boolean = false
)

open class User(
        @PrimaryKey var id: Int? = null,
        var name: String? = null,
        var username: String? = null,
        var html_url: String? = null,
        var avatar_url: String? = null,
        var bio: String? = null,
        var location: String? = null,
        var bucket_count: Int? = null,
        var comments_received_count: Int? = null,
        var followers_count: Int? = null,
        var followings_count: Int? = null,
        var likes_count: Int? = null,
        var likes_received_count: Int? = null,
        var projects_count: Int? = null,
        var rebounds_received_count: Int? = null,
        var shots_count: Int? = null,
        var teams_count: Int? = null,
        var can_upload_shot: Boolean? = null,
        var type: String? = null,
        var pro: Boolean? = null,
        var buckets_url: String? = null,
        var followers_url: String? = null,
        var following_url: String? = null,
        var likes_url: String? = null,
        var shots_url: String? = null,
        var teams_url: String? = null,
        var created_at: String? = null,
        var updated_at: String? = null
): RealmObject()

open class Followers(
        @PrimaryKey var id: Int? = null,
        var name: String? = null,
        var username: String? = null,
        var html_url: String? = null,
        var avatar_url: String? = null,
        var bio: String? = null,
        var flag: Boolean? = null,
        var isFollowing: Boolean = false
): RealmObject()

open class Following(
        @PrimaryKey var id: Int? = null,
        var name: String? = null,
        var username: String? = null,
        var avatar_url: String? = null,
        var bio: String? = null,
        var flag: Boolean? = null
): RealmObject()

open class  NewFollower(
        @PrimaryKey var id: Int? = null,
        var name: String? = null,
        var username: String? = null,
        var avatar_url: String? = null,
        var bio: String? = null,
        var isFollowing: Boolean = false
): RealmObject()

open class NewUnfollower(
        @PrimaryKey var id: Int? = null,
        var name: String? = null,
        var username: String? = null,
        var avatar_url: String? = null,
        var bio: String? = null,
        var isFollowing: Boolean = false
): RealmObject()

open class NotFollowingBack(
        @PrimaryKey var id: Int? = null,
        var name: String? = null,
        var username: String? = null,
        var avatar_url: String? = null,
        var bio: String? = null,
        var isFollowing: Boolean = true
): RealmObject()

open class Friends(
        @PrimaryKey var id: Int? = null,
        var name: String? = null,
        var username: String? = null,
        var avatar_url: String? = null,
        var bio: String? = null,
        var isFollowing: Boolean = true
): RealmObject()

open class Fans(
        @PrimaryKey var id: Int? = null,
        var name: String? = null,
        var username: String? = null,
        var avatar_url: String? = null,
        var bio: String? = null,
        var isFollowing: Boolean = false
): RealmObject()

