package com.onlinetalentsearchexam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arianinstitute.databinding.ActivityExamFinishedBinding

class ExamFinishedActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityExamFinishedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityExamFinishedBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.goBackBtn.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}