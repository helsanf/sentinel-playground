package com.edtslib.data.source

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.edtslib.SentinelAlarmReceiver
import com.edtslib.Sentinel
import com.edtslib.data.source.local.ConfigurationLocalSource
import com.edtslib.data.source.local.SentinelQueueLocalDataSource
import com.edtslib.data.source.local.entity.Configuration
import com.edtslib.data.source.remote.SentinelRemoteDataSource
import com.edtslib.data.source.remote.request.SentinelApplicationRequest
import com.edtslib.data.source.remote.request.SentinelCoreRequest
import com.edtslib.data.source.remote.request.SentinelNetworkRequest
import com.edtslib.data.source.remote.request.SentinelRequest
import com.edtslib.data.source.remote.request.SentinelUserRequest
import com.edtslib.domain.repository.ISentinelRepository
import kotlinx.coroutines.flow.flow
import java.util.*

class SentinelRepository(
    private val remoteSource: SentinelRemoteDataSource,
    private val localSource: SentinelQueueLocalDataSource,
    private val configurationLocalSource: ConfigurationLocalSource,
    private val context: Context
) : ISentinelRepository {
    private var alarmManager: AlarmManager? = null

    override fun createSession() = flow {
        val sessionId = UUID.randomUUID().toString()
        var configuration = configurationLocalSource.getCached()
        if (configuration == null) {
            configuration = Configuration(sessionId, 0L, null)
        } else {
            configuration.sessionId = sessionId
        }

        configurationLocalSource.save(configuration)

        emit(sessionId)
    }

    override fun setUserId(userId: Long) = flow {
        var configuration = configurationLocalSource.getCached()
        if (configuration == null) {
            configuration = Configuration(
                sessionId = "",
                userId = userId
            )
        }

        configuration.userId = userId
        configurationLocalSource.save(configuration)

        emit(userId)
    }

    override fun setLatLng(lat: Double?, lng: Double?) = flow {
        var configuration = configurationLocalSource.getCached()
        if (configuration == null) {
            configuration = Configuration(
                sessionId = "",
                userId = 0L,
                latitude = lat,
                longitude = lng
            )
        }

        configuration.latitude = lat
        configuration.longitude = lng
        configurationLocalSource.save(configuration)

        emit(true)
    }

    override fun track(
        group: String,
        name: String,
        details: Any?,
        userDetails : Any?
    ) = flow {
        val eventId = configurationLocalSource.getEventId()
        val user = Sentinel.getUser()

        val coreRequest = SentinelCoreRequest(
            eventAction = group,
            eventName = name,
            eventId = eventId,
            eventTimeStamp = Date().time.toString(),
            eventSource = "android",
            details = details,
            userDetails = userDetails

        )

        val userRequest = SentinelUserRequest(
            userId = user.userId,
            userName = user.userName,
            sessionId = configurationLocalSource.getSessionId()
        )

        val data = SentinelRequest(
            core = coreRequest,
            user = userRequest,
            application = SentinelApplicationRequest.create(context),
            network = SentinelNetworkRequest.create(null, null),
        )

        val size = localSource.getCached()?.size ?: 0
        if (Sentinel.flushSize > 0 && (size+1) >= Sentinel.flushSize) {
            localSource.add(data)
            doSubmit()
            return@flow
        }


        if (Sentinel.flushInterval > 0) {
            localSource.add(data)
            if (alarmManager == null) {
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    123,
                    Intent(context, SentinelAlarmReceiver::class.java),
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )

                alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager?.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + Sentinel.flushInterval * 1000,
                    pendingIntent
                )
            }
        }

        emit(true)
    }

    override fun submit() = flow {
        doSubmit()

        emit(true)
    }

    private suspend fun doSubmit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            alarmManager?.cancelAll()
        }
        else {
            alarmManager?.cancel {  }
        }

        alarmManager = null

        val cached = localSource.getCached()
        if(cached?.isNotEmpty() == true){
            remoteSource.send(cached)
            localSource.clear()
        }

    }
}