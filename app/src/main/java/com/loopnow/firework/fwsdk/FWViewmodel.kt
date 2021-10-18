package com.loopnow.firework.fwsdk

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.CoroutineContext

class FWViewModel(application: Application) : AndroidViewModel(application) {

    var auth = MutableLiveData<String>()

   suspend fun authorize(strUrl: String, clientId: String, guestId: String) {

        viewModelScope.launch(context = Dispatchers.IO) {
            val url = URL(strUrl)
            val httpConn = url.openConnection()
            httpConn.setRequestProperty("Accept", "application/json")

            FWSDk.fwUserAgent?.let{
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
            auth.postValue(response.toString())
            Log.e("FWViewModel",""+ response.toString())
        }
    }
}