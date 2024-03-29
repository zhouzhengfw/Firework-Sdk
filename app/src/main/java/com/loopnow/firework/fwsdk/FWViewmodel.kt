package com.loopnow.firework.fwsdk

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.api.Input
import com.google.gson.Gson
import com.loopnow.firework.CreateDiscoverFeedMutation
import com.loopnow.firework.fwsdk.beans.AuthBean
import com.loopnow.firework.net.FireWorkGraphqlClient.mutate
import com.loopnow.firework.net.OkHttpClient.httpOkHttClient
import kotlinx.coroutines.*
import okhttp3.*

import okio.IOException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.CoroutineContext

class FWViewModel(application: Application) : AndroidViewModel(application) {

    //    var auth = Muta
    private val client = OkHttpClient()


    fun authorize1(strUrl: String, clientId: String, guestId: String): JSONObject {
        val url = URL(strUrl)
        val httpConn = url.openConnection()
        httpConn.setRequestProperty("Accept", "application/json")

        FWSDk.fwUserAgent?.let {
            Log.v("NetworkLog", "$it")
            httpConn.setRequestProperty("User-Agent", it)
        }

        httpConn.doOutput = true
        val params = "grant_type=guest_uid&scope=openid&client_id=$clientId&guest_uid=$guestId"



        Log.v("NetworkLog", "$strUrl -> $params ")
        httpConn.outputStream.use {
            it.write(params.toByteArray(Charsets.UTF_8))
            it.flush()
            it.close()
        }

        val response = StringBuilder()

        val rc = (httpConn as HttpURLConnection).responseCode
        if (rc != 201 && rc != 200) {

            val errorObject = JSONObject()
            errorObject.put("error", rc)
            throw Exception(errorObject.toString())

        }

        // Ideally you want to check if coroutine scope is cancelled
        BufferedReader(InputStreamReader(httpConn.inputStream, Charsets.UTF_8)).use { br ->
            var responseLine: String?
            responseLine = br.readLine()
            while (responseLine != null) {
                Log.v("NetworkLog", responseLine)

                response.append(responseLine.trim())
                responseLine = br.readLine()
            }
        }
        return JSONObject(response.toString())

    }

    fun authorize(
        strUrl: String,
        clientId: String,
        guestId: String,
        callback: (auth: AuthBean) -> Unit = {}
    ) {
        var auth: AuthBean? = null

        viewModelScope.launch(context = Dispatchers.IO) {
            val formBody: RequestBody = FormBody.Builder()
                .add("grant_type", "guest_uid")
                .add("scope", "openid")
                .add("client_id", clientId)
                .add("guest_uid", guestId)
                .build()
            val request = Request.Builder()
                .header("User-Agent", FWSDk.fwUserAgent ?: "")
                .header("Accept", "application/json")
                .post(formBody)
                .url(strUrl).build()

            kotlin.runCatching {
                OkHttpClient.Builder().build().newCall(request).execute().use { response ->

                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    var res = response.body()!!.string().apply {
                        println(this)
                    }
                    auth = Gson().fromJson(res, AuthBean::class.java)
                    Log.e("FWViewModel", "" + res)
                    auth?.let {
                        callback(it)
                    }
                }

            }.onFailure {
                Log.e("FWViewModel", "" + it.message)

            }

        }
    }

    fun getFeed() {
        viewModelScope.launch {
            Log.e("getFeed", "")
            var data = mutate(CreateDiscoverFeedMutation(Input.fromNullable(1)))

            kotlin.runCatching {
                Log.e("getFeed", Gson().toJson(data))

            }

        }
    }
}