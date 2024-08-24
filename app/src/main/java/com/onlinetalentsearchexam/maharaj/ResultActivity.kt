package com.onlinetalentsearchexam.maharaj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.arianinstitute.R
import com.arianinstitute.databinding.ActivityResultBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
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
        window.statusBarColor=getColor(R.color.white)

        val studentId = Paper.book().read("userid","")
        val map: MutableMap<String, RequestBody> = mutableMapOf()
        map.apply {
            put("exam_taken_id", createPartFromString(Global.exam_taken_id1!!))
            put("student_id", createPartFromString(studentId!!))
            put("exam_id", createPartFromString(Global.examid))
        }
        Log.d("TAGGG","examtakenid:"+Global.exam_taken_id1+" studentId:"+studentId)
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
            data.data[0].apply {
                mBinding.correct.text=correct.toString()
                mBinding.incorrect.text=incorrect.toString()
                mBinding.totalQuestion.text=(correct!! + incorrect!!).toString()
                mBinding.outOf.text=(correct!! + incorrect!!).toString()
                mBinding.totalMarks.text=correct.toString()
                initPieChart(data.data[0])
            }
        }
    }
    private fun initPieChart(result:Result){
        mBinding.apply {
            val pieEntryList= arrayListOf<PieEntry>()
            pieEntryList.apply {
                add(PieEntry(result.correct!!.toFloat(),"Correct"))
                add(PieEntry(result.incorrect!!.toFloat(),"Incorrect"))
            }
            val pieDataSet=PieDataSet(pieEntryList,"Answers")
            pieDataSet.apply {
//                colors=ColorTemplate.MATERIAL_COLORS.asList()
                colors = listOf(resources.getColor(R.color.green),resources.getColor(R.color.red))
            }
            pieChart.apply {
                data= PieData(pieDataSet)
                description.isEnabled=false
                animateY(1500)
                invalidate()
            }
        }
    }
}