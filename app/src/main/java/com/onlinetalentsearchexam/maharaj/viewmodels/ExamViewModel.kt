package com.onlinetalentsearchexam.maharaj.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onlinetalentsearchexam.maharaj.CorrectAnsUseCase
import com.onlinetalentsearchexam.maharaj.data.models.IntroResponse
import com.onlinetalentsearchexam.maharaj.IntroUseCase
import com.onlinetalentsearchexam.maharaj.QuizAnsSubmitUsecase
import com.onlinetalentsearchexam.maharaj.QuizDetailUseCase
import com.onlinetalentsearchexam.maharaj.QuizQuestionUseCase
import com.onlinetalentsearchexam.maharaj.QuizResultSubmitUsecase
import com.onlinetalentsearchexam.maharaj.ResultUseCase
import com.onlinetalentsearchexam.maharaj.data.models.CommonResponse
import com.onlinetalentsearchexam.maharaj.data.models.CorrectAnsResponse
import com.onlinetalentsearchexam.maharaj.data.models.QuizDetailResponse
import com.onlinetalentsearchexam.maharaj.data.models.QuizAnsSubmitRequest
import com.onlinetalentsearchexam.maharaj.data.models.QuizAnsSubmitResponse
import com.onlinetalentsearchexam.maharaj.data.models.QuizQuestionResponse
import com.onlinetalentsearchexam.maharaj.data.models.QuizResultSubmitRequest
import com.onlinetalentsearchexam.maharaj.data.models.ResultResponse
import com.onlinetalentsearchexam.maharaj.retrofit.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject


@HiltViewModel
class ExamViewModel @Inject constructor(
    val introUseCase: IntroUseCase,
    val correctAnsUseCase: CorrectAnsUseCase,
    val resultUseCase: ResultUseCase,
    val questionAnswerUseCase: QuizQuestionUseCase,
    val quizAnsSubmitUsecase: QuizAnsSubmitUsecase,
    val quizDetailUseCase: QuizDetailUseCase,
    val quizResultSubmitUsecase: QuizResultSubmitUsecase
) : ViewModel() {
      var introResponse:MutableLiveData<State<IntroResponse>>  = MutableLiveData()
    var questionAnswerResponse:MutableLiveData<State<QuizQuestionResponse>>  = MutableLiveData()
    var correctAnsResponse:MutableLiveData<State<CorrectAnsResponse>>  = MutableLiveData()
    var resultResponse:MutableLiveData<State<ResultResponse>> = MutableLiveData()
    var quizResultSubmitResponse:MutableLiveData<State<CommonResponse>> = MutableLiveData()
    var quizAnsSubmitResponse:MutableLiveData<State<QuizAnsSubmitResponse>> = MutableLiveData()
    var quizDetailResponse:MutableLiveData<State<QuizDetailResponse>> = MutableLiveData()

    fun getIntro() {
       viewModelScope.launch {
           introUseCase.invoke().collect(){
               introResponse.value=it
           }
       }
   }
    fun getQuizDetail(partMap: MutableMap<String, RequestBody>) {
        viewModelScope.launch {
            quizDetailUseCase.invoke(partMap).collect(){
                    quizDetailResponse.value=it
            }
        }
    }
    fun getQuestionAnswer(partMap: MutableMap<String, RequestBody>) {
        viewModelScope.launch {
            questionAnswerUseCase.invoke(partMap).collect(){
                questionAnswerResponse.value=it
            }
        }
    }
    fun quizAnsSubmit(data:QuizAnsSubmitRequest) {
        viewModelScope.launch {
            quizAnsSubmitUsecase.invoke(data).collect(){
                quizAnsSubmitResponse.value=it
            }
        }
    }
    fun quizResultSubmit(data:QuizResultSubmitRequest) {
        viewModelScope.launch {
            quizResultSubmitUsecase.invoke(data).collect{
                quizResultSubmitResponse.value=it
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
