package com.dreamakasa.stabbble.common.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class RxBasePresenter<T: BaseView>: BasePresenter<T>(){
    private val compositeDisposable = CompositeDisposable()

    fun addDisposable(disposable: Disposable){
        compositeDisposable.add(disposable)
    }

    fun clearDisposable(){
        compositeDisposable.clear()
    }
}