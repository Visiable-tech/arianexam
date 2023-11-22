package com.onlinetalentsearchexam.maharaj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.arianinstitute.R
import com.arianinstitute.databinding.ActivityInstructionBinding
import com.arianinstitute.databinding.CustomviewIntroBinding
import com.onlinetalentsearchexam.DashboardActivity
import com.onlinetalentsearchexam.maharaj.data.models.IntroResponse
import com.onlinetalentsearchexam.maharaj.retrofit.State
import com.onlinetalentsearchexam.maharaj.viewmodels.ExamViewModel
import com.visiabletech.avision.maharaj.core.extension.observe
import com.visiabletech.avision.maharaj.core.util.Utility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstructionActivity : AppCompatActivity() {
    lateinit var binding:ActivityInstructionBinding
    private val introViewModel : ExamViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityInstructionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor=getColor(R.color.white)

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
}