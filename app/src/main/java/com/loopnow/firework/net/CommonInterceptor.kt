package com.loopnow.firework.net


import android.util.Log
import com.loopnow.firework.fwsdk.FWSDk
import com.loopnow.firework.utils.FwUtil
import okhttp3.*



/**
 * Create by zhouzheng
 */
class CommonInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val build = chain.request().newBuilder().apply {
               addHeader("session_id", "${FWSDk.sdkSessionId?:""}")
               addHeader("visitor_id", "${FWSDk.sdkUserId?:""}")
               addHeader("User-Agent", "${FWSDk.fwUserAgent?:""}")
               addHeader("Accept-Language", FwUtil.getAcceptLanguage())
                FWSDk.auth?.let {
                   addHeader("Authorization", "bearer: ${it.access_token}")
                }
            }
        var response:Response? = null
        kotlin.runCatching {
            var request = build.build()
            Log.e("request",request.toString()?:"")

            response = chain.proceed(request)
        }.onFailure {
            Log.e("onFailure",it.message?:"")
        }


        return response!!
    }

}