package com.onlinetalentsearchexam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.arianinstitute.R
import com.arianinstitute.databinding.ActivityMainBinding
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.onlinetalentsearchexam.maharaj.InstructionActivity
import io.paperdb.Paper

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var appUpdateManager: AppUpdateManager
    private val MY_REQUEST_CODE = 123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor=getColor(R.color.white)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding=DataBindingUtil.setContentView(this, R.layout.activity_main)
        appUpdateManager = AppUpdateManagerFactory.create(this)
        checkForUpdates()
        Handler(Looper.getMainLooper()).postDelayed({
            if(Paper.book().read("login",0)==1){
                val intent = Intent(this, InstructionActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 3000)
    }
    private fun checkForUpdates() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update
                try {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this,
                        MY_REQUEST_CODE
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                // Show dialog to force the update
                showUpdateDialog()
            }
        }
    }

    private fun showUpdateDialog() {
        AlertDialog.Builder(this)
            .setTitle("Update Required")
            .setMessage("This app requires an update to continue. Please update to the latest version.")
            .setPositiveButton("Update") { _, _ ->
                checkForUpdates()
            }
            .setNegativeButton("Exit") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }
}