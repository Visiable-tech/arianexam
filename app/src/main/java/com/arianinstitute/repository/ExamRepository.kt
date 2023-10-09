package com.arianinstitute.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.arianinstitute.model.Login
import com.arianinstitute.model.examtest.Examtest
import com.arianinstitute.response.ExamResponse
import com.nctapplication.commons.ApiInterface
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ExamRepository
@Inject
constructor(private val retroInstance: ApiInterface){
    val TAG = javaClass.simpleName

    fun examtest(requestBody: RequestBody): MutableLiveData<ExamResponse> {
        var mutableList: MutableLiveData<ExamResponse> = MutableLiveData()

        retroInstance.examtest(requestBody).enqueue(object: Callback<Examtest> {

                override fun onResponse(call: Call<Examtest>, response: Response<Examtest>) {


                    if(response.isSuccessful && response.body()!=null){
                        mutableList.postValue(ExamResponse(response.body()))
                    } else {
                        val error: Throwable? = null
                        mutableList.postValue(ExamResponse(error))
                    }
                }

                override fun onFailure(call: Call<Examtest>, t: Throwable) {
                    Log.e(TAG, "onFailure call="+call.toString()+"\n"+t.message )
                    mutableList.postValue(ExamResponse(t))
                }

            })


        return mutableList
    }
}