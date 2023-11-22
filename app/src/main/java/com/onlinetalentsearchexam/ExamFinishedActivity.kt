package com.onlinetalentsearchexam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arianinstitute.databinding.ActivityExamFinishedBinding
import com.onlinetalentsearchexam.maharaj.CorrectAnsActivity
import com.onlinetalentsearchexam.maharaj.ResultActivity

class ExamFinishedActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityExamFinishedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityExamFinishedBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.apply {
            correctAns.setOnClickListener{
                startActivity(Intent(this@ExamFinishedActivity,CorrectAnsActivity::class.java))
            }
            viewResult.setOnClickListener {
                startActivity(Intent(this@ExamFinishedActivity, ResultActivity::class.java))
            }
            goBackBtn.setOnClickListener {
                startActivity(Intent(this@ExamFinishedActivity, DashboardActivity::class.java))
                finish()
            }
        }
    }
}