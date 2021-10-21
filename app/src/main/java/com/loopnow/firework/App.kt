package com.loopnow.firework

import android.app.Application

import com.loopnow.firework.fwsdk.FWSDk

class App:Application() {
    private val client_id = "f6d6ec1275217f178cdff91363825cb390e038c1168f64f6efa23cb95ec6b325" //"f6d6ec1275217f178cdff91363825cb390e038c1168f64f6efa23cb95ec6b325"

    override fun onCreate() {
        super.onCreate()
        FWSDk.initSdk(this,client_id,"123jdk")
    }

}