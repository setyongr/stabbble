package com.dreamakasa.stabbble.injection.module

import android.content.SharedPreferences
import com.dreamakasa.stabbble.data.model.Pref
import com.dreamakasa.stabbble.data.remote.AuthenticationService
import com.dreamakasa.stabbble.data.remote.StabbbleService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule{
    @Provides
    @Singleton
    fun provideRetrofit(pref: SharedPreferences): Retrofit {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor {
            chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder().method(original.method(), original.body())
            val token: String? = pref.getString(Pref.ACCESS_TOKEN, null)
            if(token != null) requestBuilder.addHeader("Authorization", "Bearer $token")
            val request = requestBuilder.build()
            chain.proceed(request)
        }

        return Retrofit.Builder()
                .baseUrl(StabbbleService.ENDPOINT)
                .client(httpClient.build())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun provideStabbbleService(retrofit: Retrofit): StabbbleService =
            retrofit.create(StabbbleService::class.java)

    @Provides
    @Singleton
    fun provideAuthenticationService() : AuthenticationService{
        val retrofit = Retrofit.Builder()
                .baseUrl(AuthenticationService.ENDPOINT)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return retrofit.create(AuthenticationService::class.java)
    }
}