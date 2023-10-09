package com.arianinstitute.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.arianinstitute.model.Login
import com.arianinstitute.model.SubmitTest
import com.arianinstitute.model.examtest.Examtest
import com.arianinstitute.response.SubmittestResponse
import com.nctapplication.commons.ApiInterface
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SubmittestRepository
@Inject
constructor(private val retroInstance: ApiInterface){
    val TAG = javaClass.simpleName

    fun submit_test(requestBody: RequestBody): MutableLiveData<SubmittestResponse> {
        var mutableList: MutableLiveData<SubmittestResponse> = MutableLiveData()

        retroInstance.submit_test(requestBody).enqueue(object: Callback<SubmitTest> {

                override fun onResponse(call: Call<SubmitTest>, response: Response<SubmitTest>) {


                    if(response.isSuccessful && response.body()!=null){
                        mutableList.postValue(SubmittestResponse(response.body()))
                    } else {
                        val error: Throwable? = null
                        mutableList.postValue(SubmittestResponse(error))
                    }
                }

                override fun onFailure(call: Call<SubmitTest>, t: Throwable) {
                    Log.e(TAG, "onFailure call="+call.toString()+"\n"+t.message )
                    mutableList.postValue(SubmittestResponse(t))
                }

            })


        return mutableList
    }
}