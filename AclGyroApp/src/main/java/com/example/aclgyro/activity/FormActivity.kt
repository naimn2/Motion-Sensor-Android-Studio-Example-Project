package com.example.aclgyro.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.example.aclgyro.CommonUser
import com.example.aclgyro.R
import com.example.aclgyro.model.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_form.*

class FormActivity : AppCompatActivity(), View.OnClickListener {
    private val jenisKelaminItems = arrayOf("Pria", "Wanita")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        btn_activityForm_lanjut.setOnClickListener(this)
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, jenisKelaminItems)
        spinner_activityForm_jenisKelamin.adapter = spinnerAdapter
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            when (p0.id){
                R.id.btn_activityForm_lanjut -> putDataToDB()
            }
        }
    }

    private fun putDataToDB() {
        val namaLengkap: String = et_activityForm_namaLengkap.text.toString()
        val umur: Int = et_activityForm_umur.text.toString().toInt()
        val jenisKelamin: Int = spinner_activityForm_jenisKelamin.selectedItemPosition
        val user: User = User(namaLengkap, umur, jenisKelamin)

        val fdb: FirebaseDatabase = FirebaseDatabase.getInstance()
        val mRef: DatabaseReference = fdb.getReference()
        val mKey: String = mRef.push().key.toString()
        mRef.child(mKey).setValue(user)
        CommonUser.setIdCommonUser(this, mKey)

        updateUI()
        finish()
    }

    private fun updateUI(){
        val mIntent: Intent = Intent(this, MainActivity::class.java)
        startActivity(mIntent)
    }
}