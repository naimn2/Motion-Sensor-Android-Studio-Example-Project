package com.example.aclgyro.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.aclgyro.CommonUser
import com.example.aclgyro.R

class OpeningActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opening)

        Handler().postDelayed(Runnable {
            var mIntent: Intent?
            if (CommonUser.getIdCommonUser(applicationContext) != null){ // sudah pernah login
                mIntent = Intent(this, MainActivity::class.java)
            } else { // belum pernah login
                mIntent = Intent(this, FormActivity::class.java)
            }
            if (mIntent!=null){
                startActivity(mIntent)
                finish()
            }
        }, 1500)
    }
}