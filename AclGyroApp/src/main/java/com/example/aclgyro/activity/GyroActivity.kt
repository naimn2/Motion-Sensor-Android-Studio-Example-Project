package com.example.aclgyro.activity

import android.content.DialogInterface
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.aclgyro.CommonUser
import com.example.aclgyro.R
import com.example.aclgyro.model.Coordinate
import com.example.aclgyro.model.DataSensor
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_gyro.*
import java.util.*
import kotlin.collections.ArrayList

class GyroActivity : AppCompatActivity(), SensorEventListener, ValueEventListener {
    private val TAG: String = GyroActivity::class.java.simpleName
    private val JENIS_SENSOR: Int = 1
    private lateinit var sensorManager : SensorManager
    private lateinit var gyro : Sensor
    private lateinit var dataSensorList: ArrayList<DataSensor>
    private lateinit var coordinateList: ArrayList<Coordinate>
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private var time2update : Boolean = true
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var isRecording: Boolean = false
    private var label: Int = 0

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

        dataSensorList = ArrayList()
        coordinateList = ArrayList()
        firebaseDatabase = FirebaseDatabase.getInstance()
        val uid: String? = CommonUser.getIdCommonUser(this)
        databaseReference = firebaseDatabase.getReference(uid.toString())

        btn_gyro_record.setOnClickListener(View.OnClickListener {
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
        btn_gyro_record.text = "Stop"
        btn_gyro_record.setBackgroundResource(R.color.merah_pucat)
        Toast.makeText(this, "Recording Started", Toast.LENGTH_SHORT).show()
    }

    private fun postRecording(){
        isRecording = false
        endTime = Date().time
        btn_gyro_record.text = "Record"
        btn_gyro_record.setBackgroundResource(R.color.biru_pucat)
        Toast.makeText(this, "Recording Stopped", Toast.LENGTH_SHORT).show()

        showPopupClassifier()
    }

    private fun showPopupClassifier(){
        val classifierView: View = layoutInflater.inflate(R.layout.dialog_classifier_sensor, null, false)
        val classSpinner: Spinner = classifierView.findViewById(R.id.spinner_dialogClassifierSensor)

        val classesArrayAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, DataSensor.JENIS_JENIS_AKTIVITAS)
        classSpinner.adapter = classesArrayAdapter

        AlertDialog.Builder(this)
                .setView(classifierView)
                .setTitle("Set Label")
                .setPositiveButton("Submit", object: DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        label = classSpinner.selectedItemPosition
                        updateDb()
                    }
                })
                .show()
    }

    override fun onStop() {
        super.onStop()
        if (isRecording){
            postRecording()
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, gyro, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0 != null) {
            if (time2update) {
                if (Math.abs(p0.values[0]) >= 0.1 - SENSITIVITY/10 || Math.abs(p0.values[1]) >= 0.1 - SENSITIVITY/10 || Math.abs(p0.values[2]) >= 0.1 - SENSITIVITY/10) {
                    tv_gyro_x.text = String.format("%.012f", p0.values[0])
                    tv_gyro_y.text = String.format("%.012f", p0.values[1])
                    tv_gyro_z.text = String.format("%.012f", p0.values[2])

                    time2update = false
                    Handler().postDelayed(Runnable {
                        time2update = true
                    }, DELAY)

                    if (isRecording) {
                        coordinateList.add(Coordinate(p0.values[0], p0.values[1], p0.values[2]))
                        Log.d(TAG, "coordinates length: "+coordinateList.size)
                    }
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

        val newDataSensor = DataSensor()
        newDataSensor.jenisSensor = JENIS_SENSOR
        newDataSensor.startTime = startTime
        newDataSensor.endTime = endTime
        newDataSensor.gyroCoords = coordinateList
        newDataSensor.label = label
        dataSensorList.add(newDataSensor)
        snapshot.ref.setValue(dataSensorList)
        Toast.makeText(this, "Record just saved", Toast.LENGTH_SHORT).show()
    }

    override fun onCancelled(error: DatabaseError) {

    }
}