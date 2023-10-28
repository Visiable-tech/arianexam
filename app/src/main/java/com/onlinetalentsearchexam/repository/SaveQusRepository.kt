package com.onlinetalentsearchexam.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.onlinetalentsearchexam.model.savequestion.SaveQus
import com.onlinetalentsearchexam.response.SaveQusResponse
import com.avision.commons.ApiInterface
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SaveQusRepository
@Inject
constructor(private val retroInstance: ApiInterface){
    val TAG = javaClass.simpleName

    fun save_question(requestBody: RequestBody): MutableLiveData<SaveQusResponse> {
        var mutableList: MutableLiveData<SaveQusResponse> = MutableLiveData()

        retroInstance.save_question(requestBody).enqueue(object: Callback<SaveQus> {

                override fun onResponse(call: Call<SaveQus>, response: Response<SaveQus>) {


                    if(response.isSuccessful && response.body()!=null){
                        mutableList.postValue(SaveQusResponse(response.body()))
                    } else {
                        val error: Throwable? = null
                        mutableList.postValue(SaveQusResponse(error))
                    }
                }

                override fun onFailure(call: Call<SaveQus>, t: Throwable) {
                    Log.e(TAG, "onFailure call="+call.toString()+"\n"+t.message )
                    mutableList.postValue(SaveQusResponse(t))
                }

            })


        return mutableList
    }
}