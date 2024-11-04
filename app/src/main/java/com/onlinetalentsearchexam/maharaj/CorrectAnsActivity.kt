package com.onlinetalentsearchexam.maharaj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.arianinstitute.R
import com.arianinstitute.databinding.ActivityCorrectAnsBinding
import com.google.gson.Gson
import com.onlinetalentsearchexam.DataHolder
import com.onlinetalentsearchexam.Global
import com.onlinetalentsearchexam.maharaj.data.models.CorrectAnsResponse
import com.onlinetalentsearchexam.maharaj.data.models.Question
import com.onlinetalentsearchexam.maharaj.retrofit.State
import com.onlinetalentsearchexam.maharaj.viewmodels.ExamViewModel
import com.visiabletech.avision.maharaj.core.extension.createPartFromString
import com.visiabletech.avision.maharaj.core.extension.observe
import com.visiabletech.avision.maharaj.core.util.Utility
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.RequestBody

@AndroidEntryPoint
class CorrectAnsActivity : AppCompatActivity() {
    lateinit var mBinding:ActivityCorrectAnsBinding
//    private val viewModel:ExamViewModel by viewModels()
    var data: List<Question>?= listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityCorrectAnsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
//        data= Gson().fromJson(intent.getStringExtra("data"),Array<Question>::class.java).toList()
        data=DataHolder.data
        if(data!=null){
            initDataToView()
        }
        mBinding.backBtn.setOnClickListener{onBackPressed()}
//        val map: MutableMap<String, RequestBody> = mutableMapOf()
//        map.apply {
//            put("test_id", createPartFromString(Global.examid))
//        }
//        viewModel.apply {
//            observe(correctAnsResponse,::onCorrectAnsReceive)
//            getCorrectAns(map)
//        }
    }

//    private fun onCorrectAnsReceive(state: State<CorrectAnsResponse>?) {
//        when (state) {
//            is State.ErrorState -> {
//                Utility.performErrorState(this,state, "Failed")
//            }
//            is State.DataState -> {
//                initDataToView(state.data)
//            }
//            else -> {}
//        }
//    }

//    fun initDataToView(data: CorrectAnsResponse){
//        mBinding.quesRecyclerView.apply {
//            layoutManager=LinearLayoutManager(context)
//            adapter=CorrectAnswerQuestionAdapter(context,data.message!!)
//        }
//    }
    fun initDataToView(){
        mBinding.quesRecyclerView.apply {
            layoutManager=LinearLayoutManager(context)
            adapter=CorrectAnswerQuestionAdapter(context,data!!)
        }
    }

}