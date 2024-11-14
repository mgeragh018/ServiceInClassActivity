package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var timerBinder: TimerService.TimerBinder? = null
    lateinit var timerView : TextView;
    val timerHandler = Handler(Looper.getMainLooper()){
        timerView.text=it.what.toString()
        true
    }
    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            timerBinder = service as TimerService.TimerBinder
            timerBinder!!.setHandler(timerHandler)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            timerBinder = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        timerView = findViewById<TextView>(R.id.textView)
        findViewById<Button>(R.id.startButton).setOnClickListener {
            bindService(
                Intent(this, TimerService::class.java),
                serviceConnection,
                BIND_AUTO_CREATE
            )
            findViewById<Button>(R.id.startButton).setOnClickListener {

                if (timerBinder?.isRunning == false) {
                    timerBinder?.start(100)
                } else  {
                    timerBinder?.pause()
                }
            }


            findViewById<Button>(R.id.stopButton).setOnClickListener {
                timerBinder?.stop()
            }
        }

    }
}