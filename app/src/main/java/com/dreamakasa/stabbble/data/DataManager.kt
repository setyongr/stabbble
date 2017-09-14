package com.dreamakasa.stabbble.data

import com.dreamakasa.stabbble.data.remote.StabbbleService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManager @Inject constructor(private val service: StabbbleService){
}