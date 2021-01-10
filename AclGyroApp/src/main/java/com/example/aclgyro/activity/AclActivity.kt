package com.example.aclgyro.activity

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.aclgyro.CommonUser
import com.example.aclgyro.R
import com.example.aclgyro.model.Coordinate
import com.example.aclgyro.model.DataSensor
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_acl.*
import java.util.*
import kotlin.collections.ArrayList

class AclActivity : AppCompatActivity(), SensorEventListener, ValueEventListener {
    private val TAG : String = AclActivity::class.java.simpleName
    private val JENIS_SENSOR: Int = 0
    private lateinit var sensorManager : SensorManager
    private lateinit var acl : Sensor
    private lateinit var dataSensorList: ArrayList<DataSensor>
    private lateinit var coordinateList: ArrayList<Coordinate>
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private var time2update : Boolean = true
    private var startTime: Long = 0
    private var endTime: Long = 0

    private val SENSITIVITY : Float = 0.8f // 0-1
    private val DELAY : Long = 100 // ms
    private var isRecording: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acl)
        title = "Accelerometer"

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        acl = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        if (acl == null){
            Toast.makeText(this, "HP anda kentang", Toast.LENGTH_SHORT).show()
            finish()
        }

        dataSensorList = ArrayList()
        coordinateList = ArrayList()
        firebaseDatabase = FirebaseDatabase.getInstance()
        val uid: String? = CommonUser.getIdCommonUser(this)
        databaseReference = firebaseDatabase.getReference(uid.toString())

        btn_acl_record.setOnClickListener(View.OnClickListener {
            if (!isRecording){
                preRecording()
            } else {
                postRecording()
            }
        })
    }

    private fun preRecording(){
        isRecording = true
        coordinateList.clear()
        dataSensorList.clear()
        startTime = Date().time
        btn_acl_record.text = "Stop"
        btn_acl_record.setBackgroundResource(R.color.merah_pucat)
    }

    private fun postRecording(){
        isRecording = false
        endTime = Date().time
        btn_acl_record.text = "Record"
        btn_acl_record.setBackgroundResource(R.color.biru_pucat)
        updateDb()
        Toast.makeText(this, "Saving record to db ...", Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        if (isRecording){
            postRecording()
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, acl, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0 != null) {
            if (time2update) {
                if (Math.abs(p0.values[0]) >= 0.1 - SENSITIVITY/10 || Math.abs(p0.values[1]) >= 0.1 - SENSITIVITY/10 || Math.abs(p0.values[2]) >= 0.1 - SENSITIVITY/10) {
                    tv_acl_x.text = String.format("%.012f", p0.values[0])
                    tv_acl_y.text = String.format("%.012f", p0.values[1])
                    tv_acl_z.text = String.format("%.012f", p0.values[2])

                    time2update = false
                    Handler().postDelayed(Runnable {
                        time2update = true
                    }, DELAY)

                    coordinateList.add(Coordinate(p0.values[0], p0.values[1], p0.values[2]))
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.d(TAG, "onAccuracyChanged: ")
//        Toast.makeText(this, "Accuracy Changed", Toast.LENGTH_SHORT).show()
    }

    private fun updateDb(){
        databaseReference.child(DataSensor.DATABASE_REFERENCE).addListenerForSingleValueEvent(this)
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        if (snapshot.hasChildren()) {
            snapshot.children.forEach {
                val dataSensor: DataSensor = it.getValue(DataSensor::class.java) as DataSensor
                dataSensorList.add(dataSensor)
            }
        }

        val newDataSensor: DataSensor = DataSensor(JENIS_SENSOR, startTime, endTime, coordinateList, true)
        dataSensorList.add(newDataSensor)
        snapshot.ref.setValue(dataSensorList)
        Toast.makeText(this, "record just saved", Toast.LENGTH_SHORT).show()
    }

    override fun onCancelled(error: DatabaseError) {

    }
}