package com.example.aclgyro.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.aclgyro.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_aclSensor.setOnClickListener(this)
        btn_gyroSensor.setOnClickListener(this)
        btn_aclGyroSensor.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            var mIntent: Intent? = null
            when (p0.id){
                R.id.btn_aclSensor -> mIntent = Intent(applicationContext, AclActivity::class.java)
                R.id.btn_gyroSensor -> mIntent = Intent(applicationContext, GyroActivity::class.java)
                R.id.btn_aclGyroSensor -> mIntent = Intent(applicationContext, AclGyroActivity::class.java)
            }
            if (mIntent != null){
                startActivity(mIntent)
            }
        }
    }
}