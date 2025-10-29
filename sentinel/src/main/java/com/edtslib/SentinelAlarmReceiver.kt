package com.edtslib

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.edtslib.domain.repository.ISentinelRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

class SentinelAlarmReceiver: BroadcastReceiver(), KoinComponent {
    private val repository: ISentinelRepository by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.e("==="," onReceive")
            repository.submit().collect()
        }
    }
}