package com.loopnow.firework.fwsdk

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.loopnow.firework.fwsdk.beans.AuthBean
import com.loopnow.firework.fwsdk.preference.sharedpreferences.FWStorage
import com.loopnow.firework.net.FireWorkGraphqlClient
import com.loopnow.firework.type.CustomType
import com.loopnow.firework.utils.FwConsts
import com.loopnow.firework.utils.FwConsts.TOKEN_RECEIVED_TIME
import com.loopnow.firework.utils.FwUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.*

class FWSDk {

    companion object {
        var encodedClientId: String = ""
        var userId: String = ""
        var sdkSessionId: String = ""
        var fwVm: FWViewModel? = null
        var fwUserAgent: String? = null
        var sdkUserId: String? = null
        var sdkClientId: String? = null
        var sdkGuestId: String? = null

        var auth:AuthBean? = null
        @SuppressLint("StaticFieldLeak")
        var app: Context? = null

        @JvmStatic
        fun initSdk(application: Context, clientId: String, userId: String?) {
            app = application
            sdkUserId = userId
            sdkClientId = clientId
            sdkSessionId = newSessionId()


            sdkGuestId = if (FWStorage.contains(FwConsts.GUEST_ID)) {
                FWStorage.getString(FwConsts.GUEST_ID, UUID.randomUUID().toString())
            } else {
                if (userId.isNullOrEmpty()) UUID.randomUUID().toString() else userId
            }

            FWStorage.putSting(FwConsts.GUEST_ID, sdkGuestId!!,isSyn = true)

            fwUserAgent = FwUtil.getUserAgentInfo(application, application.packageName.toString())
            Log.e("initSdk",fwUserAgent+"")
            fwVm = ViewModelProvider.AndroidViewModelFactory.getInstance(app as Application)
                .create(FWViewModel::class.java)

            GlobalScope.launch {
                fwVm?.authorize1(FwUtil.getHost() + FwConsts.OAUTH_URL,  clientId ,  sdkGuestId!!)
//                {
//                    auth = it
//                    FWStorage.putLong(TOKEN_RECEIVED_TIME,System.currentTimeMillis(),true)
//                    val idToken = it.id_token
//                    if (!idToken.isNullOrBlank()) {
//                        decoded(idToken)
//                    } else {
//                        //User login
//                        it?.let {
//                            FWStorage.putSting(FwConsts.USER_LOGIN_ACCESS_TOKEN, it.access_token)
//                            FWStorage.putSting(FwConsts.USER_LOGIN_REFRESH_TOKEN, it.refresh_token)
//                            FWStorage.putInt(FwConsts.USER_LOGIN_REFRESH_TOKEN_EXPIRES_IN, it.refresh_token_expires_in)
//                            FWStorage.putLong(FwConsts.USER_LOGIN_CREATED_AT, System.currentTimeMillis())
//                        }
//                    }
//                }
//                fwVm?.getFeed()
            }
//            FireWorkGraphqlClient.init()

//            app.registerActivityLifecycleCallbacks()
        }

        private fun newSessionId() = UUID.randomUUID().toString()

        fun decoded(input: String) {
            try {
                val arr =
                    input.split("\\.".toRegex()).toTypedArray()
                val decodedBytes = Base64.decode(arr[0], 0)
                val header = String(decodedBytes, Charset.forName("UTF-8"))
                Log.d("userInfo", "user: " + header)
//
                val infos = Base64.decode(arr[1], 0)
                val userInfo = String(infos, Charset.forName("UTF-8"))
                val bodyJson = JSONObject(userInfo)
                Log.d("userInfo", "bodyJson: " + bodyJson)

                userId = bodyJson.getString("user_id")
//
                encodedClientId = bodyJson.getString("oaid")


            } catch (e: UnsupportedEncodingException) {
                Log.v("SdkLog", e.toString())
                //Error
            }
        }
    }
}