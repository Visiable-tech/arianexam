package com.onlinetalentsearchexam.maharaj.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onlinetalentsearchexam.maharaj.CorrectAnsUseCase
import com.onlinetalentsearchexam.maharaj.data.models.IntroResponse
import com.onlinetalentsearchexam.maharaj.IntroUseCase
import com.onlinetalentsearchexam.maharaj.ResultUseCase
import com.onlinetalentsearchexam.maharaj.data.models.CorrectAnsResponse
import com.onlinetalentsearchexam.maharaj.data.models.ResultResponse
import com.onlinetalentsearchexam.maharaj.retrofit.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject


@HiltViewModel
class ExamViewModel @Inject constructor(
    val introUseCase: IntroUseCase,
    val correctAnsUseCase: CorrectAnsUseCase,
    val resultUseCase: ResultUseCase
) : ViewModel() {
      var introResponse:MutableLiveData<State<IntroResponse>>  = MutableLiveData()
    var correctAnsResponse:MutableLiveData<State<CorrectAnsResponse>>  = MutableLiveData()
    var resultResponse:MutableLiveData<State<ResultResponse>> = MutableLiveData()
    fun getIntro() {
       viewModelScope.launch {
           introUseCase.invoke().collect(){
               introResponse.value=it
           }
       }
   }
    fun getCorrectAns(partMap: MutableMap<String, RequestBody>) {
        viewModelScope.launch {
            correctAnsUseCase.invoke(partMap).collect(){
                correctAnsResponse.value=it
            }
        }
    }
    fun getResult(partMap: MutableMap<String, RequestBody>){
        viewModelScope.launch {
            resultUseCase.invoke(partMap).collect(){
                resultResponse.value=it
            }
        }
    }
}
