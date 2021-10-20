package com.loopnow.firework.net

import android.content.Context
import android.os.Looper
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.*
import com.apollographql.apollo.api.cache.http.HttpCachePolicy

import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloNetworkException
import com.loopnow.firework.BuildConfig
import com.loopnow.firework.fwsdk.FWSDk
import com.loopnow.firework.fwsdk.componets.AlertLoadingDialog
import com.loopnow.firework.fwsdk.componets.AppLifecycleManager
import com.loopnow.firework.fwsdk.componets.INetWorkLoading
import com.loopnow.firework.fwsdk.componets.NetWorkPlaceholder

import com.loopnow.firework.net.OkHttpClient.httpOkHttClient
import com.loopnow.firework.utils.FwConsts
import okhttp3.OkHttpClient

import java.io.File
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*


object FireWorkGraphqlClient {
    const val GRAPHQL_PATH = "/graphiql"
    private lateinit var instance: ApolloClient


    fun init( ) {
        instance = ApolloClient.builder()
            .serverUrl(if (com.loopnow.firework.BuildConfig.DEBUG) FwConsts.HOST_STAGING+ GRAPHQL_PATH else FwConsts.HOST_PRODUCTION + GRAPHQL_PATH)
            .okHttpClient(httpOkHttClient)
//            .addCustomTypeAdapter(scalarType, DateTimeApolloAdapter())
            .build()
    }

    fun graphqlClient(): ApolloClient {
        return instance
    }


    suspend fun <D : Operation.Data, T, V : Operation.Variables> query(query: Query<D, T, V>, isLoading: Boolean = false, context: Context = AppLifecycleManager.presentActivity ?: FWSDk.app!!,
                                                                       loading: INetWorkLoading? = AlertLoadingDialog(context), isShowError: Boolean = false, cachePolicy: HttpCachePolicy.Policy = HttpCachePolicy.NETWORK_ONLY, client: ApolloClient = instance, isAsyn: Boolean = false,
                                                                       netWorkPlaceholder: NetWorkPlaceholder? = null): T? {

        if (!isAsyn) {
            check(Looper.myLooper() == Looper.getMainLooper()) {
                "Only the main thread can get the apolloClient instance"
            }
        }

        if (!isAsyn) {
            if (isLoading) {
                loading?.startLoading()
            }
        }
        val response = try {
            client.query(query).toBuilder()
                .httpCachePolicy(cachePolicy).build().await()
        } catch (e: Exception) {
            if (e is ApolloNetworkException) {
                AppLifecycleManager.presentActivity?.runOnUiThread {
                }
            }
            AppLifecycleManager.presentActivity?.runOnUiThread {
                netWorkPlaceholder?.showPlaceholder()
            }

            null
        }
        if (response != null) {
            AppLifecycleManager.presentActivity?.runOnUiThread {
                netWorkPlaceholder?.hidePlaceholder()
            }
        }

        if (!isAsyn) {
            if (isLoading) {
                loading?.endLoading()
            }

        }

        return response?.data
    }

    suspend fun <D : Operation.Data, T, V : Operation.Variables> mutate(mutation: Mutation<D, T, V>, isLoading: Boolean = false, context: Context = AppLifecycleManager.presentActivity ?: FWSDk.app!!,
                                                                        loading: INetWorkLoading? = AlertLoadingDialog(context), isShowError: Boolean = false, setErrorTextAction: (text: String) -> Unit = {}, isAsyn: Boolean = false): T? {
        if (!isAsyn) {
            check(Looper.myLooper() == Looper.getMainLooper()) {
                "Only the main thread can get the apolloClient instance"
            }
        }
        if (!isAsyn) {
            if (isLoading) {
                loading?.startLoading()
            }
        }

        val response = try {
            instance.mutate(mutation).await()
        } catch (e: Exception) {
            if (e is ApolloNetworkException) {
                AppLifecycleManager.presentActivity?.runOnUiThread {
                }
            }
            null
        }

        if (!isAsyn) {
            if (isLoading) {
                loading?.endLoading()
            }

        }
        return response?.data
    }


}

object OkHttpClient {

    val httpOkHttClient: OkHttpClient

    init {
        //信任所有证书解决ssl问题
        val buffer = OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(CommonInterceptor())
        try {
            val trustAllCerts: Array<X509TrustManager> = arrayOf(object : X509TrustManager {

                override fun checkClientTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) {
                }

                override fun checkServerTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) {
                }

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                    return arrayOf()
                }
            })

            val sc: SSLContext = SSLContext.getInstance("TLS")
            sc.init(null, trustAllCerts, SecureRandom())
            buffer
                .sslSocketFactory(sc.socketFactory, trustAllCerts[0])
                .hostnameVerifier { hostname, session -> true }

        } catch (e: java.lang.Exception) {

        }
        httpOkHttClient = buffer.build()

    }

}


class DateTimeApolloAdapter : CustomTypeAdapter<Date> {
    override fun decode(value: CustomTypeValue<*>): Date {
        return try {
            val date = value.value as String
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").apply {
                timeZone = TimeZone.getTimeZone("GMT")
            }.parse(date)!!
        } catch (e: Exception) {
            e.printStackTrace()
            Date()
        }
    }

    override fun encode(value: Date): CustomTypeValue<*> = CustomTypeValue.fromRawValue(value)

}










