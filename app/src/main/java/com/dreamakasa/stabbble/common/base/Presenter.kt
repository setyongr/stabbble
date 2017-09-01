package com.dreamakasa.stabbble.common.base

interface Presenter<in V: BaseView>{
    fun attachView(view: V)
    fun detachView()
}