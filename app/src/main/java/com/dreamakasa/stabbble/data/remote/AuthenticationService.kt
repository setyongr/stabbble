package com.dreamakasa.stabbble.data.remote

import com.dreamakasa.stabbble.data.model.SampleReq
import com.dreamakasa.stabbble.data.model.TokenRes
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthenticationService{
    companion object {
        val ENDPOINT = "https://dribbble.com/oauth/"
    }

    @FormUrlEncoded
    @POST("token")
    fun getAccessToken(
            @Field("client_id") client_id: String,
            @Field("client_secret") client_secret: String,
            @Field("code") code: String,
            @Field("redirect_uri") redirect_uri: String
    ): Observable<TokenRes>
}