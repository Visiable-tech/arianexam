package com.onlinetalentsearchexam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.arianinstitute.R
import com.arianinstitute.databinding.ActivityExamFinishedBinding
import com.google.gson.Gson
import com.onlinetalentsearchexam.maharaj.CorrectAnsActivity
import com.onlinetalentsearchexam.maharaj.ResultActivity
import com.onlinetalentsearchexam.maharaj.data.models.Question

class ExamFinishedActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityExamFinishedBinding
     var data: List<Question>?= listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityExamFinishedBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
//        data=Gson().fromJson(intent.getStringExtra("data"),Array<Question>::class.java).toList()
        data= DataHolder.data
        window.statusBarColor=getColor(R.color.white)


        mBinding.apply {

            correctAns.setOnClickListener{
                val intent=Intent(this@ExamFinishedActivity,CorrectAnsActivity::class.java)
//                    .putExtra("data",Gson().toJson(data))
                startActivity(intent)
            }
//            viewResult.setOnClickListener {
//                startActivity(Intent(this@ExamFinishedActivity, ResultActivity::class.java))
//            }
            goBackBtn.setOnClickListener {
                backpress()
            }

        }
    }

    override fun onBackPressed() {
        backpress()
    }
    fun backpress(){
        startActivity(Intent(this@ExamFinishedActivity, DashboardActivity::class.java))
        finish()
    }
}