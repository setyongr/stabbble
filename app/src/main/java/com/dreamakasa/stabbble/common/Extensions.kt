package com.dreamakasa.stabbble.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import okhttp3.Response

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View =
        LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)

fun Response.count():Int{
    var count: Int = 1
    var res: Response? = this
    while (res?.priorResponse() != null){
        res = res.priorResponse()
        count++
    }
    return count
}