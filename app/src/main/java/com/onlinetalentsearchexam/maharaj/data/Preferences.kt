package com.onlinetalentsearchexam.maharaj.data

import android.content.Context
import android.preference.PreferenceManager
import androidx.core.content.edit
import com.google.gson.Gson

class Preferences(context: Context) {
     val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun storeString(data: String, key: String) {
        preferences.edit {
            putString(key, data)
        }
    }

//    fun storeInt(data: Int, key: String) {
//        preferences.edit {
//            putInt(key, data)
//        }
//    }
//
//    fun storeBoolean(data: Boolean, key: String) {
//        preferences.edit {
//            putBoolean(key, data)
//        }
//    }
//    fun storeUser(data: User) {
//        val mUserAccount = Gson().toJson(data)
//        storeString(mUserAccount, Constants.KEY_USER)
//    }
//    fun fetchUser(): User? {
//        val mUserAccount = fetchString(Constants.KEY_USER,"")
//        return if (mUserAccount.isNotEmpty()) {
//            Gson().fromJson(mUserAccount, User::class.java)
//        } else {
//            null
//        }
//    }
//    fun clearAllPreferences() {
//        preferences.edit {
//            clear()
//        }
//    }
//    fun clearPreference(key: String) {
//        preferences.edit(commit = true) {
//            remove(key)
//        }
//    }









    fun fetchString(key: String, defaultValue: String = ""): String {
        return preferences.getString(key, defaultValue) ?: defaultValue
    }
}