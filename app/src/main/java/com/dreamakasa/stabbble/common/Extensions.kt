package com.dreamakasa.stabbble.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dreamakasa.stabbble.R
import com.tapadoo.alerter.Alerter
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

fun Alerter.showDefaultError(title:String, msg: String){
    this.setTitle(title)
            .setText(msg)
            .setIcon(R.drawable.ic_close_black_24dp)
            .setBackgroundColorRes(R.color.red)
            .enableSwipeToDismiss()
            .enableInfiniteDuration(true)
            .show()
}