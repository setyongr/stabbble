package com.dreamakasa.stabbble.data

import com.dreamakasa.stabbble.data.remote.BackboneService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManager @Inject constructor(private val service: BackboneService){
    fun getTest() = service.getSample()
}