package com.onlinetalentsearchexam.maharaj

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.arianinstitute.R
import com.arianinstitute.databinding.ActivityInstructionBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.onlinetalentsearchexam.DashboardActivity
import com.onlinetalentsearchexam.maharaj.data.models.IntroResponse
import com.onlinetalentsearchexam.maharaj.retrofit.State
import com.onlinetalentsearchexam.maharaj.viewmodels.ExamViewModel
import com.visiabletech.avision.maharaj.core.extension.observe
import com.visiabletech.avision.maharaj.core.util.Utility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstructionActivity : AppCompatActivity() {
    private val REQUEST_CODE_UPDATE = 123
    private lateinit var appUpdateManager: AppUpdateManager

    lateinit var binding:ActivityInstructionBinding
    private val introViewModel : ExamViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityInstructionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor=getColor(R.color.white)


        appUpdateManager = AppUpdateManagerFactory.create(this)

        // Check for updates
        checkForAppUpdate()
        introViewModel.apply {
            observe(introResponse,:: onReceiveIntroData)
            getIntro()
        }
    }

    override fun onBackPressed() {
        Toast.makeText(this,"kindly read instruction carefully \n& proceed by pressing Understood button",Toast.LENGTH_LONG).show()
    }
    private fun onReceiveIntroData(state: State<IntroResponse>?) {
        binding.okBtn.visibility= View.VISIBLE
        when (state) {
            is State.ErrorState -> {
                Utility.performErrorState(this,state, "Failed")
            }
            is State.DataState -> {
                introDataToView(state.data)
            }
            else -> {}
        }
    }
    fun introDataToView(data: IntroResponse){
         binding.apply {
            title.text=data.message?.title
//            desc.text=data.message?.content
            desc.loadDataWithBaseURL(null,data.message?.content!!,"text/html", "utf-8", null)
            okBtn.setOnClickListener {
                startActivity(Intent(applicationContext,DashboardActivity::class.java))
                finishAffinity()
            }

         }
    }

    private fun checkForAppUpdate() {
        // Fetch the update availability information
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            // Check if an update is available and is allowed for immediate update
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                // Request an immediate update
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    REQUEST_CODE_UPDATE
                )
            }
        }
    }

    // Handle the update result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_UPDATE) {
            if (resultCode != Activity.RESULT_OK) {
                // If the update fails or is canceled, notify the user
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Update failed! Please try again.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    // Optional: handle the case when the user navigates back to the app without completing the update
    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                // Resume the update if it was previously interrupted
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    REQUEST_CODE_UPDATE
                )
            }
        }
    }
}