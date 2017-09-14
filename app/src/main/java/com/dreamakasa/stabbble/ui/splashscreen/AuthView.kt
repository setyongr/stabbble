package com.dreamakasa.stabbble.ui.splashscreen

import com.dreamakasa.stabbble.common.base.BaseView

interface AuthView : BaseView{
    fun loginSuccess()
    fun loginError(title: String, content: String)
}