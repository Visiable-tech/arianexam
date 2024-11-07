package com.avision.commons
import com.onlinetalentsearchexam.model.Login

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST("svclogin")
    fun login(@Body body: RequestBody?): Call<Login>



}