package com.dreamakasa.stabbble.data.remote

import com.dreamakasa.stabbble.data.model.TokenRes
import com.dreamakasa.stabbble.data.model.SampleReq
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST


interface BackboneService{
    companion object {
        val ENDPOINT = "https://yourapi.com/"
    }

    @GET("/")
    fun getSample(): Observable<SampleReq>

    //Return call because it will called synchronously
    @POST("/refresh")
    fun refreshToken(): Call<TokenRes>
}