package com.onlinetalentsearchexam.maharaj

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.widget.Toast
import androidx.activity.viewModels
import com.arianinstitute.R
import com.arianinstitute.databinding.ActivityResultBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.onlinetalentsearchexam.Global
import com.onlinetalentsearchexam.maharaj.data.models.Result
import com.onlinetalentsearchexam.maharaj.data.models.ResultResponse
import com.onlinetalentsearchexam.maharaj.retrofit.State
import com.onlinetalentsearchexam.maharaj.viewmodels.ExamViewModel
import com.visiabletech.avision.maharaj.core.extension.createPartFromString
import com.visiabletech.avision.maharaj.core.extension.observe
import com.visiabletech.avision.maharaj.core.util.Utility
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import okhttp3.RequestBody

@AndroidEntryPoint
class ResultActivity : AppCompatActivity() {
    lateinit var mBinding:ActivityResultBinding
    val viewModel:ExamViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityResultBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val studentId = Paper.book().read("userid","")
        var examTakenId=""
        if(Global.exam_taken_id1!="0")
            examTakenId= Global.exam_taken_id1
        else{
            examTakenId= Global.exan_taken_id2
        }
        val map: MutableMap<String, RequestBody> = mutableMapOf()
        map.apply {
            put("exam_taken_id", createPartFromString(examTakenId!!))
            put("student_id", createPartFromString(studentId!!))
        }
        viewModel.apply { 
            getResult(map)
            observe(resultResponse,::onReceiveResult)
        }
        mBinding.backBtn.setOnClickListener{onBackPressed()}
    }

    private fun onReceiveResult(state: State<ResultResponse>?) {
        when (state) {
            is State.ErrorState -> {
                Utility.performErrorState(this,state, "Failed")
            }
            is State.DataState -> {
                initDataToView(state.data)
            }
            else -> {}
        }
    }

    private fun initDataToView(data: ResultResponse) {
        if(data.data==null || !data.message.isNullOrEmpty()){
            Toast.makeText(this,data.message,Toast.LENGTH_LONG).show()
            onBackPressed()
        }else{
            data.data.apply {
                mBinding.correct.text=correct.toString()
                mBinding.incorrect.text=incorrect.toString()
                mBinding.totalQuestion.text=(correct!! + incorrect!!).toString()
                initPieChart(data.data)
            }
        }
    }
    private fun initPieChart(data:Result){
        mBinding.apply {
            val pieEntryList= arrayListOf<PieEntry>()
            pieEntryList.apply {
                add(PieEntry(data.correct!!.toFloat(),"Correct"))
                add(PieEntry(data.incorrect!!.toFloat(),"Incorrect"))
            }
            val pieDataSet=PieDataSet(pieEntryList,"Answers")
            pieDataSet.apply {
//                setColors(ColorTemplate.MATERIAL_COLORS,this@ResultActivity)
                colors = listOf(resources.getColor(R.color.green),resources.getColor(R.color.red))
            }
            pie.data= PieData(pieDataSet)
            pie.description.isEnabled=false
            pie.animateY(1500)
            pie.invalidate()
        }
    }
}