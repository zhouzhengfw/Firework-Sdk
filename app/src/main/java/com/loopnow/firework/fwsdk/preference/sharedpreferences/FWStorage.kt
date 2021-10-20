package com.loopnow.firework.fwsdk.preference.sharedpreferences

import com.loopnow.firework.fwsdk.FWSDk
import com.google.gson.Gson
import java.lang.reflect.Type

/**
 * Create by zhouzheng
 */
object FWStorage {
    const val DEFAULT_SP_NAME = "fwConfig"

    fun putSting(key: String, value: String, isSyn: Boolean = false,spName: String = DEFAULT_SP_NAME) {
        BoosterSharedPreferences.getSharedPreferences(FWSDk.app,spName)?.edit()
            ?.apply {
                putString(key, value)
                if (isSyn) {
                    commit()
                } else {
                    apply()
                }
            }
    }

    fun contains( key: String):Boolean{
      return BoosterSharedPreferences.getSharedPreferences(FWSDk.app, DEFAULT_SP_NAME).contains(key)
    }

    fun putInt(key: String, value: Int, isSyn: Boolean = false,spName: String = DEFAULT_SP_NAME) {
        BoosterSharedPreferences.getSharedPreferences(FWSDk.app,spName)?.edit()
            ?.apply {
                putInt(key, value)
                if (isSyn) {
                    commit()
                } else {
                    apply()
                }
            }
    }

    fun putLong( key: String, value: Long, isSyn: Boolean = false,spName: String = DEFAULT_SP_NAME) {
       BoosterSharedPreferences.getSharedPreferences(FWSDk.app,spName)?.edit()
            ?.apply {
                putLong(key, value)
                if (isSyn) {
                    commit()
                } else {
                    apply()
                }
            }
    }

    fun putBoolean( key: String, value: Boolean, isSyn: Boolean = false,spName: String = DEFAULT_SP_NAME) {
       BoosterSharedPreferences.getSharedPreferences(FWSDk.app,spName)?.edit()
            ?.apply {
                putBoolean(key, value)
                if (isSyn) {
                    commit()
                } else {
                    apply()
                }
            }
    }

    fun <T> putObject( key: String, value: T, isSyn: Boolean = false,spName: String = DEFAULT_SP_NAME) {
       BoosterSharedPreferences.getSharedPreferences(FWSDk.app,spName)?.edit()
            ?.apply {
                putString(key, Gson().toJson(value))
                if (isSyn) {
                    commit()
                } else {
                    apply()
                }
            }
    }


    fun getString( key: String, default: String = "",spName: String = DEFAULT_SP_NAME): String {
        return BoosterSharedPreferences.getSharedPreferences(FWSDk.app,spName)
            .getString(key, default) ?: default
    }

    fun getInt( key: String, default: Int = 0,spName: String = DEFAULT_SP_NAME): Int {
        return BoosterSharedPreferences.getSharedPreferences(FWSDk.app,spName)
            .getInt(key, default)
    }

    fun getLong(key: String, default: Long = 0,spName: String = DEFAULT_SP_NAME): Long {
        return BoosterSharedPreferences.getSharedPreferences(FWSDk.app,spName)
            .getLong(key, default)
    }

    fun getBoolean( key: String, default: Boolean = false,spName: String = DEFAULT_SP_NAME): Boolean {
        return BoosterSharedPreferences.getSharedPreferences(FWSDk.app,spName)
            .getBoolean(key, default)
    }

    fun <T> getObject(key: String, type: Type,spName: String = DEFAULT_SP_NAME): T? {
        return Gson().fromJson<T>(BoosterSharedPreferences
            .getSharedPreferences(FWSDk.app,spName)
            .getString(key, ""), type)
    }

    fun <T> getObject( key: String, type: Class<T>,spName: String = DEFAULT_SP_NAME): T? {
        return Gson().fromJson<T>(BoosterSharedPreferences
            .getSharedPreferences(FWSDk.app,spName)
            .getString(key, ""), type)
    }

    fun delete(key: String, isSyn: Boolean = false,spName: String = DEFAULT_SP_NAME) {
       BoosterSharedPreferences.getSharedPreferences(FWSDk.app,spName)?.edit()
            ?.apply {
                remove(key)
                if (isSyn) {
                    commit()
                } else {
                    apply()
                }
            }
    }


}