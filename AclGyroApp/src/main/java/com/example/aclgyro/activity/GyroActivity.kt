package com.example.aclgyro.activity

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.example.aclgyro.R
import kotlinx.android.synthetic.main.activity_gyro.*

class GyroActivity : AppCompatActivity(), SensorEventListener {
    private val TAG: String = GyroActivity::class.java.simpleName
    private lateinit var sensorManager: SensorManager
    private lateinit var gyro: Sensor
    private var time2update : Boolean = true

    private val SENSITIVITY : Float = 0.8f // 0-1
    private val DELAY : Long = 100 // ms

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gyro)

        title = "Gyroscope Sensor"

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        if (gyro == null){
            Toast.makeText(this, "HP anda kentang", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, gyro, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0!=null){
            if (time2update) {
                if (Math.abs(p0.values[0]) >= 0.1 - SENSITIVITY / 10 || Math.abs(p0.values[1]) >= 0.1 - SENSITIVITY / 10 || Math.abs(p0.values[2]) >= 0.1 - SENSITIVITY / 10) {
                    tv_gyro_x.text = String.format("%.012f", p0.values[0])
                    tv_gyro_y.text = String.format("%.012f", p0.values[1])
                    tv_gyro_z.text = String.format("%.012f", p0.values[2])

                    time2update = false
                    Handler().postDelayed(Runnable {
                        time2update = true
                    }, DELAY)
                }
            }

//            if (p0.values[0] > 0.5f){
//                window.decorView.setBackgroundColor(Color.BLUE)
//            } else if (p0.values[0] < -0.5f) {
//                window.decorView.setBackgroundColor(Color.RED)
//            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.d(TAG, "onAccuracyChanged: ")
    }
}