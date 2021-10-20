package com.loopnow.firework.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.res.Resources
import android.media.AudioManager
import android.os.Build
import android.os.LocaleList
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.os.ConfigurationCompat
import androidx.viewbinding.BuildConfig
import java.lang.RuntimeException
import java.math.BigInteger
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


object FwUtil {

    val fireowrkSDKVersion = "v5.6.0"


    fun getNowTimeISO(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(Calendar.getInstance().time)
    }

    fun getNowTimeISOLocal(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US)
        dateFormat.timeZone = Calendar.getInstance().timeZone
        return dateFormat.format(Calendar.getInstance().time)
    }


    fun getCountryCode(): String {
        val current = ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0)
        return current.country
    }

    fun getCurrentLanguage(): String {
        val current = ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0)
        return current.language
    }

    fun getCurrentVolume(context: Context): Int {
        val audio = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return audio.getStreamVolume(AudioManager.STREAM_MUSIC)
    }

    //Firework SDK version
    fun getVersion(): String {
        return fireowrkSDKVersion
    }

    //Product name of firework SDK
    fun getProductName(): String {
        return "embed.android.sdk"
    }

    fun getOS(): String {
        return "android"
    }

    fun getPlatform(): String {
        return "android_sdk"
    }

    fun getTrackVersion(): String {
        return "v3.0.0"
    }

    fun getPlayUid(): String {
        return System.currentTimeMillis().toString()
    }

    fun getAndroidVersion(): String {
        return Build.VERSION.RELEASE
    }

    fun getDeviceCarrier(context: Context): String {
        val manager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return manager.networkOperatorName
    }

    fun getLanguage(): String {
        return Locale.getDefault().toLanguageTag()
    }

    fun getAppInfo(context: Context): String? {
        context?.let {
            var pInfo: PackageInfo = it.packageManager.getPackageInfo(it.packageName, 0)
            pInfo?.let {
                return pInfo.versionName
            }
        }
    }

    private fun getApplicationName(context: Context): String? {
        val applicationInfo =
            context.packageManager.getApplicationInfo(context.applicationInfo.packageName, 0);
        return if (applicationInfo != null) context.packageManager.getApplicationLabel(
            applicationInfo
        ).toString() else "Unknown"

    }

    fun getAcceptLanguage(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.getDefault().toLanguageTags()
        } else {
            Locale.getDefault().language
        }
    }

    fun getUserAgentInfo(context: Context, bundleId: String): String {

        val productName =
            getProductName() //Need to discuss if unify all of the product name on the different event reports.

        val result = System.getProperty("http.agent") +
                "[FWAN/" + productName + ";" +
                "FWCA/" + getApplicationName(context) + ";" +
                "FWAV/" + fireowrkSDKVersion + ";" +
                "FWCN/" + bundleId + ";" +
                "FWCV/" + getAppInfo(context) + ";" +
                "FWCR/" + getDeviceCarrier(context) + ";" +
                "FWDV/" + Build.MODEL + ";" +
                "FWLC/" + getLanguage() + ";" +
                "FWMD/" + Build.MANUFACTURER + ";" +
                "FWSN/" + getOS() + ";" +
                "FWBI/" + context.packageName + ";" +
                "FWSV/" + getAndroidVersion() + "]"

        return URLEncoder.encode(result, "utf-8").replace("+", "%20")
    }

    fun format(num: Float): String {
        val enlocale = Locale("en", "US")
        val df = DecimalFormat.getNumberInstance(enlocale)
        df.maximumFractionDigits = 2
        return df.format(num)
    }

    fun format(num: Double): String {
        val enlocale = Locale("en", "US")
        val df = DecimalFormat.getNumberInstance(enlocale)
        df.maximumFractionDigits = 2
        return df.format(num)
    }

    fun sha256Hash(str: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(str.toByteArray())
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }


    fun String.md5(): String {
        return try {
            // Static getInstance method is called with hashing MD5
            val md = MessageDigest.getInstance("MD5")
            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            val messageDigest = md.digest(this.toByteArray())
            // Convert byte array into signum representation
            val no = BigInteger(1, messageDigest)
            // Convert message digest into hex value
            var hashtext = no.toString(16)
            while (hashtext.length < 32) {
                hashtext = "0$hashtext"
            }
            hashtext
        } // For specifying wrong message digest algorithms
        catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }

    /**
     * based on debug or release configuration , return host name
     *
     */
    fun getHost(): String {
        return if (com.loopnow.firework.BuildConfig.DEBUG) FwConsts.HOST_STAGING else FwConsts.HOST_PRODUCTION
    }

    fun logger(type: String, tag: String, info: String, logInDebugOnly: Boolean) {
        if (logInDebugOnly) {
            if (!com.loopnow.firework.BuildConfig.DEBUG) {
                return
            }
        }

        when (type) {
            "d" -> Log.d(tag, info)
            "e" -> Log.e(tag, info)
            "i" -> Log.i(tag, info)
            "v" -> Log.v(tag, info)
            "w" -> Log.w(tag, info)
        }
    }

    fun getVastTagUrlWithCorrelator(url: String): String? {
        if (url.endsWith("correlator=", true)) {
            return url + System.currentTimeMillis()
        }
        return url
    }

}

