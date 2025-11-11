package edts.sentinel.myapplication

import android.app.Application
import com.edtslib.Sentinel
import com.edtslib.domain.model.SentinelUser

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Sentinel.init(
            application = this,
            baseUrl = "http://staging-sentinel-api.sg-edts.co.id/tracker/",
            apiKey = "sen_g8lIuEc6DJf8LG5yAV2zdJyQXkTPLF8O3I_aYJ4hJko",
            flushInterval = 30,
            flushSize = 9,
            sessionTimeout = 1,
            getUser = {
                SentinelUser(1, "")
            }
        )
    }
}