package com.dreamakasa.stabbble.data.remote

import com.dreamakasa.stabbble.data.model.*
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface StabbbleService {
    companion object {
        val ENDPOINT = "https://api.dribbble.com/v1/"
    }

    @GET("user")
    fun getCurrentUser(): Observable<UserRes>

    @GET("user/followers")
    fun getFollowers(@Query("page") page: Int, @Query("per_page") per_page: Int): Observable<List<FollowerRes>>

    @GET("user/following")
    fun getFollowing(@Query("page") page: Int, @Query("per_page") per_page: Int): Observable<List<FollowingRes>>
}