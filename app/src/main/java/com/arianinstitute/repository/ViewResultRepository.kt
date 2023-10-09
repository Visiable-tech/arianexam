package com.arianinstitute.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.arianinstitute.model.Login
import com.arianinstitute.model.examtest.Examtest
import com.arianinstitute.model.viewresult.ViewResult
import com.arianinstitute.response.ViewResultResponse
import com.nctapplication.commons.ApiInterface
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ViewResultRepository
@Inject
constructor(private val retroInstance: ApiInterface){
    val TAG = javaClass.simpleName

    fun view_result(requestBody: RequestBody): MutableLiveData<ViewResultResponse> {
        var mutableList: MutableLiveData<ViewResultResponse> = MutableLiveData()

        retroInstance.view_result(requestBody).enqueue(object: Callback<ViewResult> {

                override fun onResponse(call: Call<ViewResult>, response: Response<ViewResult>) {

                    if(response.isSuccessful && response.body()!=null){
                        mutableList.postValue(ViewResultResponse(response.body()))
                    } else {
                        val error: Throwable? = null
                        mutableList.postValue(ViewResultResponse(error))
                    }
                }

                override fun onFailure(call: Call<ViewResult>, t: Throwable) {
                    Log.e(TAG, "onFailure call="+call.toString()+"\n"+t.message )
                    mutableList.postValue(ViewResultResponse(t))
                }

            })


        return mutableList
    }
}