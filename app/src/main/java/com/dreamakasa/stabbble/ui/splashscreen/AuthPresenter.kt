package com.dreamakasa.stabbble.ui.splashscreen

import android.content.SharedPreferences
import com.dreamakasa.stabbble.common.base.RxBasePresenter
import com.dreamakasa.stabbble.data.model.Pref
import com.dreamakasa.stabbble.data.remote.AuthenticationService
import com.dreamakasa.stabbble.data.remote.Config
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthPresenter @Inject constructor(
        private val authenticationService: AuthenticationService,
        private val pref: SharedPreferences
): RxBasePresenter<AuthView>(){
    fun getToken(code: String){
        val disposable =
                authenticationService
                        .getAccessToken(Config.CLIENT_ID, Config.CLIENT_SECRET, code, Config.REDIRECT_URI)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    pref.edit().putString(Pref.ACCESS_TOKEN, it.access_token).apply()
                                    pref.edit().putBoolean(Pref.IS_LOGGED_ID, true).apply()
                                    getView().loginSuccess()
                                },
                                {
                                    it.printStackTrace()
                                    getView().loginError("Whoopss", "Something went wrong")
                                }
                        )
        addDisposable(disposable)
    }
}