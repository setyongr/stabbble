package com.dreamakasa.stabbble.data.model

class LinksRes(
    var web: String,
    var twitter: String
)

class UserRes(
        var id: Int,
        var name: String,
        var username: String,
        var html_url: String,
        var avatar_url: String,
        var bio: String,
        var location: String,
        var links: LinksRes,
        var bucket_count: Int,
        var comments_received_count: Int,
        var followers_count: Int,
        var followings_count: Int,
        var likes_count: Int,
        var likes_received_count: Int,
        var projects_count: Int,
        var rebounds_received_count: Int,
        var shots_count: Int,
        var teams_count: Int,
        var can_upload_shot: Boolean,
        var type: String,
        var pro: Boolean,
        var buckets_url: String,
        var followers_url: String,
        var following_url: String,
        var likes_url: String,
        var shots_url: String,
        var teams_url: String,
        var created_at: String,
        var updated_at: String
)

class FollowerRes(
        var id: Int,
        var created_at: String,
        var follower: UserRes
)

class FollowingRes(
        var id: Int,
        var created_at: String,
        var followee: UserRes
)

class TokenRes(
        var access_token: String,
        var token_type: String,
        var scope: String
)

