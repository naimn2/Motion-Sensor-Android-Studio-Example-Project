package com.example.aclgyro

import android.content.Context
import android.content.SharedPreferences
import com.example.aclgyro.model.User

class CommonUser {
    companion object{
        private val USER_PREFERENCE: String = "userPreference"
        private val COMMON_USER_ID_PREFERENCE: String = "commonUserIdPreference"

        fun getIdCommonUser(context: Context): String?{
            val sp: SharedPreferences = context.getSharedPreferences(USER_PREFERENCE, 0)
            return sp.getString(COMMON_USER_ID_PREFERENCE, null)
        }
        fun setIdCommonUser(context: Context, idUser: String?){
            val sp: SharedPreferences = context.getSharedPreferences(USER_PREFERENCE, 0)
            val spe: SharedPreferences.Editor = sp.edit()
            spe.putString(COMMON_USER_ID_PREFERENCE, idUser)
            spe.apply()
        }
    }
}