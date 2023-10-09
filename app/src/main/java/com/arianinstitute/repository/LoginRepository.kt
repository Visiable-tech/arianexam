package com.arianinstitute.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.arianinstitute.model.Login
import com.arianinstitute.response.ApiResponse
import com.nctapplication.commons.ApiInterface
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class LoginRepository
@Inject
constructor(private val retroInstance: ApiInterface){
    val TAG = javaClass.simpleName

    fun login(requestBody: RequestBody): MutableLiveData<ApiResponse> {
        var mutableList: MutableLiveData<ApiResponse> = MutableLiveData()

        retroInstance.login(requestBody).enqueue(object: Callback<Login> {

                override fun onResponse(call: Call<Login>, response: Response<Login>) {


                    if(response.isSuccessful && response.body()!=null){
                        mutableList.postValue(ApiResponse(response.body()))
                    } else {
                        val error: Throwable? = null
                        mutableList.postValue(ApiResponse(error))
                    }
                }

                override fun onFailure(call: Call<Login>, t: Throwable) {
                    Log.e(TAG, "onFailure call="+call.toString()+"\n"+t.message )
                    mutableList.postValue(ApiResponse(t))
                }

            })


        return mutableList
    }
}