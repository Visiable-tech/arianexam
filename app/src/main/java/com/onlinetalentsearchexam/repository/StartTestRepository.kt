package com.onlinetalentsearchexam.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.onlinetalentsearchexam.model.start_test.StartTest
import com.onlinetalentsearchexam.response.StartTestResponse
import com.avision.commons.ApiInterface
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class StartTestRepository
@Inject
constructor(private val retroInstance: ApiInterface){
    val TAG = javaClass.simpleName

    fun start_test(requestBody: RequestBody): MutableLiveData<StartTestResponse> {
        var mutableList: MutableLiveData<StartTestResponse> = MutableLiveData()

        retroInstance.start_test(requestBody).enqueue(object: Callback<StartTest> {

                override fun onResponse(call: Call<StartTest>, response: Response<StartTest>) {


                    if(response.isSuccessful && response.body()!=null){
                        mutableList.postValue(StartTestResponse(response.body()))
                    } else {
                        val error: Throwable? = null
                        mutableList.postValue(StartTestResponse(error))
                    }
                }

                override fun onFailure(call: Call<StartTest>, t: Throwable) {
                    Log.e(TAG, "onFailure call="+call.toString()+"\n"+t.message )
                    mutableList.postValue(StartTestResponse(t))
                }

            })


        return mutableList
    }
}