package edts.sentinel.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.edtslib.Sentinel

class MainActivity : AppCompatActivity() {
    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TextView>(R.id.textView1).setOnClickListener {
            for (i in 1..9) {
                Sentinel.track("CLICK",
                    (it as TextView).text.toString(), mapOf(Pair("key", "value ${i+1}")), userDetails = mapOf(
                        Pair("tier", "gold")
                    )
                )
            }

        }

        findViewById<TextView>(R.id.textView2).setOnClickListener {
            Sentinel.track("CLICK",
                (it as TextView).text.toString(),
                mapOf(Pair("key", "value")),
                userDetails = mapOf(
                    Pair("tier", "gold")
                )
            )
        }

    }
}