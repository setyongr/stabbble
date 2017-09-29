package com.dreamakasa.stabbble.ui.main

import com.dreamakasa.stabbble.common.base.BaseView
import com.dreamakasa.stabbble.data.model.User

interface MainView: BaseView{
    fun onSyncComplete(result: AnalyticsResult)
    fun onSyncStart()
    fun onSyncError(title: String, content: String)
    fun showUserInfo(user: User)
}