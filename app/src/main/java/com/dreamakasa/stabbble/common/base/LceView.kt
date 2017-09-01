package com.dreamakasa.stabbble.common.base

interface LceView<T>: BaseView {
    fun showLoading()
    fun hideLoading()
    fun showContent(data: T)
    fun showError(e:Throwable)
}