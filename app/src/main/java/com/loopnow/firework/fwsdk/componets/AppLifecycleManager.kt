package com.loopnow.firework.fwsdk.componets

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import java.util.*

/**
 *
 * 提供APP的全局生命周期管理
 */
object AppLifecycleManager {

    private val mActivityStack = Stack<Activity>()

    private var mForegroundActivityCount = 0

    private val TAG = AppLifecycleManager::class.java.simpleName


    /**
     * 初始化⽅法，需要在Application onCreate中调⽤
     *
     * @param application 应⽤当前的Application实例
     */
    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                mActivityStack.push(activity)
            }

            override fun onActivityStarted(activity: Activity) {
                if (mForegroundActivityCount == 0) {
                    dispatchOnForeground()
                }
                mForegroundActivityCount++
            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {
                mForegroundActivityCount--
                if (mForegroundActivityCount < 0) {
                    mForegroundActivityCount = 0
                }
                if (mForegroundActivityCount == 0) {
                    dispatchOnBackground()
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {
                mActivityStack.remove(activity)
            }
        })
    }


    /**
     * 获取当前在显示的Activity实例
     *
     * @return 当前显示在前台的Activity
     */
    val presentActivity: Activity?
        get() = if (mActivityStack.isNotEmpty()) {
            mActivityStack.peek()
        } else null

    /**
     * @return 将当前所有的Activity作为list输出返回
     */
    val allActivities: List<Activity>
        get() = LinkedList(mActivityStack)

    /**
     * finish 栈中的除了参数之外的所有Activity
     *
     * @param clazz 需要保留的Activity
     */
    fun clearActivities(clazz: Class<*>?) {
        if (clazz == null) {
            dispatchOnExit()
        }
        synchronized(this) {
            for (activity in mActivityStack) {
                if (activity == null) {
                    continue
                }
                if (clazz != null && TextUtils.equals(clazz.simpleName,
                        activity.javaClass.simpleName)) {
                    continue
                }
                if (!activity.isFinishing) {
                    activity.finish()
                }
            }
        }
    }

    private val mListeners: MutableSet<AppStatusListener> = HashSet()
    private val mUIHandler = Handler(Looper.getMainLooper())

    /**
     * 注册APP状态监听器
     *
     * @param listener APP状态监听
     */
    fun registerAppStatusListener(listener: AppStatusListener) {
        mUIHandler.post { mListeners.add(listener) }
    }

    /**
     * 取消注册APP状态监听器
     *
     * @param listener APP状态监听
     */
    fun unregisterAppStatusListener(listener: AppStatusListener?) {
        mUIHandler.post { mListeners.remove(listener) }
    }

    private fun dispatchOnForeground() {
        mUIHandler.post {
            for (listener in mListeners) {
                listener.onForeground()
            }
        }
    }

    private fun dispatchOnBackground() {
        mUIHandler.post {
            for (listener in mListeners) {
                listener.onBackground()
            }
        }
    }

    private fun dispatchOnExit() {
        mUIHandler.post {
            for (listener in mListeners) {
                listener.onExit()
            }
        }
    }

    val isInForeground: Boolean
        get() = mForegroundActivityCount > 0

    /**
     * APP状态监听接⼝
     */
    interface AppStatusListener {
        /**
         * App切换到前台
         */
        fun onForeground()

        /**
         * App切换到后台
         */
        fun onBackground()

        /**
         * 清栈
         */
        fun onExit()
    }


}