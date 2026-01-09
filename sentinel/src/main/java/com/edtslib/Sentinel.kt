package com.edtslib

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import com.edtslib.di.interactorModule
import com.edtslib.di.mainAppModule
import com.edtslib.di.networkingModule
import com.edtslib.di.repositoryModule
import com.edtslib.di.sharedPreferencesModule
import com.edtslib.di.viewModule
import com.edtslib.domain.model.SentinelUser
import com.edtslib.utils.session.SessionTimeout
import com.edtslib.utils.session.SessionTimeoutDelegate
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import kotlin.getValue

class Sentinel private constructor() : KoinComponent {
    private val viewModel: SentinelViewModel? by inject()

    companion object {
        private var sentinel: Sentinel? = null

        var appVersion = ""
        var baseUrl = ""
        var apiKey = ""
        var flushInterval = 60000
        var flushSize = 100
        var debugging = false
        var sessionTimeout = 30 // in minutes
        var getUser = {
            SentinelUser(
                userId = null,
                userName = null
            )
        }

        fun init(
            application: Application,
            apiKey: String,
            flushInterval: Int = 0,
            flushSize: Int = 0,
            versionName: String? = null,
            sessionTimeout: Int = 30,
            getUser: () -> SentinelUser
        ) {

            setup(
                application = application,
                apiKey = apiKey,
                getUser = getUser,
                versionName = versionName,
                flushInterval = flushInterval,
                flushSize = flushSize,
                sessionTimeout = sessionTimeout
            )

            startKoin {
                androidContext(application)
                androidContext(application.applicationContext)
                modules(
                    listOf(
                        networkingModule,
                        sharedPreferencesModule,
                        mainAppModule,
                        repositoryModule,
                        interactorModule,
                        viewModule
                    )
                )
            }



            if (sentinel == null) {
                sentinel = Sentinel()
            }
        }

        fun init(
            application: Application,
            apiKey: String,
            koin: KoinApplication,
            versionName: String? = null,
            flushInterval: Int = 0,
            flushSize: Int = 0,
            sessionTimeout: Int = 30,
            getUser: () -> SentinelUser
        ) {
            setup(
                application = application,
                apiKey = apiKey,
                getUser = getUser,
                versionName = versionName,
                flushInterval = flushInterval,
                flushSize = flushSize,
                sessionTimeout = sessionTimeout
            )

            koin.modules(
                listOf(
                    networkingModule,
                    sharedPreferencesModule,
                    mainAppModule,
                    repositoryModule,
                    interactorModule,
                    viewModule
                )
            )

            if (sentinel == null) {
                sentinel = Sentinel()
            }
        }

        fun createSession() = sentinel?.viewModel?.createSession()

        /*** track ***/

        fun track(group : String,name: String, details: Any? = null, userDetails: Any? = null) =
            sentinel?.viewModel?.track(
                group = group,
                name = name,
                details = details,
                userDetails = userDetails
            )

        // end of track

        private fun setup(
            application: Application,
            apiKey: String,
            flushInterval: Int,
            flushSize: Int,
            versionName: String?,
            sessionTimeout: Int,
            getUser: () -> SentinelUser
        ) {

            Companion.apiKey = apiKey
            Companion.getUser = getUser
            Companion.flushInterval = flushInterval
            Companion.flushSize = flushSize
            Companion.sessionTimeout = sessionTimeout
            appVersion = versionName ?: getVersionName(application) ?: ""

            setupSessionTimeOut(application)
        }

        private fun getVersionName(application: Application) =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                application.packageManager?.getPackageInfo(
                    application.packageName, PackageManager.PackageInfoFlags.of(
                        PackageManager.GET_CONFIGURATIONS.toLong()
                    )
                )?.versionName
            } else {
                application.packageManager?.getPackageInfo(
                    application.packageName,
                    PackageManager.GET_CONFIGURATIONS
                )?.versionName
            }

        private fun setupSessionTimeOut(application: Application) {
            val sessionTimeOut = SessionTimeout(
                application = application,
                interval = sessionTimeout
            )

            sessionTimeOut.delegate = object : SessionTimeoutDelegate {
                override fun onTimeOut() {
                    sentinel?.viewModel?.doCreateSession()
                }

            }

        }
    }
}
