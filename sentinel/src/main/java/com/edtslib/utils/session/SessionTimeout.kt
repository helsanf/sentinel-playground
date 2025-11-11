package com.edtslib.utils.session

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SessionTimeout(application: Application, private val interval: Int) {
    private var minimizedRunnable: Runnable? = null
    private var minimizedStarted: Long = 0
    private var handler: Handler? = null
        get(): Handler? {
            if (field == null) {
                field = Handler(Looper.getMainLooper())
            }

            return field
        }

    var delegate: SessionTimeoutDelegate? = null

    init {
        registerActivityLifecycle(application)
    }

    private fun registerActivityLifecycle(application: Application) {
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            }

            override fun onActivityStarted(activity: Activity) {
                if (minimizedRunnable != null) {
                    handler?.removeCallbacks(minimizedRunnable!!)
                    minimizedRunnable = null

                    if (minimizedStarted > 0 && interval >= 0) {
                        val deltaMinimized = System.currentTimeMillis() - minimizedStarted
                        if (deltaMinimized > (interval *1000*60)) {
                            delegate?.onTimeOut()
                        }
                    }

                    minimizedStarted = 0
                }
            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {
                if (minimizedRunnable != null) {
                    handler?.removeCallbacks(minimizedRunnable!!)
                    minimizedRunnable = null
                }

                minimizedRunnable = Runnable {
                    minimizedStarted = System.currentTimeMillis()
                }

                handler?.postDelayed(minimizedRunnable!!, 750)
            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }

        })
    }

}