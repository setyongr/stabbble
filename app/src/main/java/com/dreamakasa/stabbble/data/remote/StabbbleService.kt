package com.dreamakasa.stabbble.data.remote

import com.dreamakasa.stabbble.data.model.TokenRes
import com.dreamakasa.stabbble.data.model.SampleReq
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST


interface StabbbleService {
    companion object {
        val ENDPOINT = "https://api.dribbble.com/v1/"
    }

    @GET("/token")
    fun getAccessToken(): Observable<SampleReq>

}