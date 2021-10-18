package com.loopnow.firework.fwsdk

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.loopnow.firework.fwsdk.preference.sharedpreferences.FWStorage
import com.loopnow.firework.utils.FwConsts
import com.loopnow.firework.utils.FwUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class FWSDk {

    companion object {
        var fwVm: FWViewModel? = null
        var fwUserAgent: String? = null
        var sdkUserId: String? = null
        var sdkClientId: String? = null
        var sdkGuestId: String? = null
        @SuppressLint("StaticFieldLeak")
        var app: Context? = null

        @JvmStatic
        fun initSdk(application: Context, clientId: String, userId: String?) {
            app = application
            sdkUserId = userId
            sdkClientId = clientId

            sdkGuestId = if (FWStorage.contains(FwConsts.GUEST_ID)) {
                FWStorage.getString(FwConsts.GUEST_ID, UUID.randomUUID().toString())
            } else {
                if (userId.isNullOrEmpty()) UUID.randomUUID().toString() else userId
            }

            FWStorage.putSting(FwConsts.GUEST_ID, sdkGuestId!!,isSyn = true)

            fwUserAgent = FwUtil.getUserAgentInfo(application, application.packageName.toString())

            fwVm = ViewModelProvider.AndroidViewModelFactory.getInstance(app as Application)
                .create(FWViewModel::class.java)

            GlobalScope.launch {
                fwVm?.authorize(FwUtil.getHost() + FwConsts.OAUTH_URL,  clientId ,  sdkGuestId!!)
            }
        }
    }
}