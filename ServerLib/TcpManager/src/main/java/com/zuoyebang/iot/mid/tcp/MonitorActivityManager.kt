package com.zuoyebang.iot.mid.tcp

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference

/**
 *
 *
 * @author 裴云飞
 * @date 2022/3/9
 */
class MonitorActivityManager private constructor() {

    private val activityRefs = ArrayList<WeakReference<Activity>>()
    private val frontBackCallbacks = ArrayList<FrontBackCallback>()
    private var activityStartCount = 0
    private var front = true

    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(InnerActivityLifecycleCallbacks())
    }

    inner class InnerActivityLifecycleCallbacks :
            Application.ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStarted(activity: Activity) {
            activityStartCount++
            //activityStartCount>0  说明应用处在可见状态，也就是前台
            //!front 之前是不是在后台
            if (!front && activityStartCount > 0) {
                front = true
                onFrontBackChanged(front)
            }
        }

        override fun onActivityDestroyed(activity: Activity) {
            for (activityRef in activityRefs) {
                if (activityRef != null && activityRef.get() == activity) {
                    activityRefs.remove(activityRef);
                    break
                }
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityStopped(activity: Activity) {
            activityStartCount--;
            if (activityStartCount <= 0 && front) {
                front = false
                onFrontBackChanged(front)
            }
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            activityRefs.add(WeakReference(activity))
        }

        override fun onActivityResumed(activity: Activity) {

        }

    }

    private fun onFrontBackChanged(front: Boolean) {
        for (callback in frontBackCallbacks) {
            callback.onChanged(front)
        }
    }

    val topActivity: Activity?
        get() {
            if (activityRefs.size <= 0) {
                return null
            } else {
                return activityRefs[activityRefs.size - 1].get()
            }
            return null
        }


    fun addFrontBackCallback(callback: FrontBackCallback) {
        if (!frontBackCallbacks.contains(callback)) {
            frontBackCallbacks.add(callback)
        }
    }

    fun removeFrontBackCallback(callback: FrontBackCallback) {
        frontBackCallbacks.remove(callback)
    }

    interface FrontBackCallback {
        fun onChanged(front: Boolean)
    }

    companion object {
        @JvmStatic
        val instance: MonitorActivityManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            MonitorActivityManager()
        }
    }
}
