package com.avision.commons

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.onlinetalentsearchexam.database.ArianDatabase
import dagger.hilt.android.HiltAndroidApp
import io.paperdb.Paper

@HiltAndroidApp
class OnlineTalentSearchExam: Application() {

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        Paper.init(applicationContext);
        dbInstance= ArianDatabase.getInstance(this)
    }


    fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.activeNetworkInfo
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    companion object {
        private var mInstance: OnlineTalentSearchExam? = null
        private lateinit var dbInstance : ArianDatabase

        @Synchronized
        fun getInstance(): OnlineTalentSearchExam? {
            return mInstance
        }

        @Synchronized
        fun getDB():ArianDatabase{
            return dbInstance
        }


        @Synchronized
        fun isValidContextForGlide(context: Context?): Boolean {
            if (context == null) {
                return false
            }
            if (context is Activity) {
                val activity = context
                if (activity.isDestroyed || activity.isFinishing) {
                    return false
                }
            }
            return true
        }
    }


}