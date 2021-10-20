package com.loopnow.firework.fwsdk

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.loopnow.firework.CreateDiscoverFeedMutation
import com.loopnow.firework.fwsdk.beans.AuthBean
import com.loopnow.firework.net.FireWorkGraphqlClient.mutate
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
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


     fun authorize1(strUrl: String, clientId: String, guestId: String) : JSONObject  {
        val url = URL(strUrl)
        val httpConn = url.openConnection()
        httpConn.setRequestProperty("Accept", "application/json")

        FWSDk.fwUserAgent?.let{
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
            errorObject.put("error",rc)
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

    fun authorize(strUrl: String, clientId: String, guestId: String,callback:(auth:AuthBean)->Unit={}) {
        var auth:AuthBean? = null

        viewModelScope.launch(context = Dispatchers.IO) {
            val params = "grant_type=guest_uid&scope=openid&client_id=$clientId&guest_uid=$guestId"

            val request = Request.Builder()
                .addHeader("User-Agent", FWSDk.fwUserAgent)
                .addHeader("Accept", "application/json")
//                .addHeader("Content-Type", "application/json")

                .url(strUrl+"?${params}")
                .build()

            FWSDk.fwUserAgent?.let{
//                httpConn.setRequestProperty("User-Agent", it)
                client
            }
            client.newCall(request).execute().use { response ->
                println(response.body()!!.string())

                if (!response.isSuccessful) throw IOException("Unexpected code $response")


                println(response.body()!!.string())

            }

        }



//            auth.postValue(response.toString())
//            auth = Gson().fromJson(response.toString(),AuthBean::class.java)
//            Log.e("FWViewModel",""+ response.toString())
//            auth?.let {
//                callback(it)
//            }

    }

    fun getFeed(){
        viewModelScope.launch {
            var data = mutate(CreateDiscoverFeedMutation())
            Log.e("getFeed",data?.createDiscoverFeed?.fragments?.videoFeed?.id?:"")
        }
    }
}