package com.dreamakasa.stabbble.ui.newfollower

import com.dreamakasa.stabbble.common.base.RxBasePresenter
import com.dreamakasa.stabbble.data.DataManager
import javax.inject.Inject

class NewFollowerPresenter @Inject constructor(private val dataManager: DataManager): RxBasePresenter<NewFollowerView>() {
    fun follow(position: Int, id: Int){
        //Do follow
    }
}