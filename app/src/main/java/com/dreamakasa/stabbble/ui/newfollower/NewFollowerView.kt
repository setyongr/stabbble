package com.dreamakasa.stabbble.ui.newfollower

import com.dreamakasa.stabbble.common.base.BaseView

interface NewFollowerView: BaseView{
    fun onFollowSuccess(position: Int)
    fun onFollowError(position: Int)
}