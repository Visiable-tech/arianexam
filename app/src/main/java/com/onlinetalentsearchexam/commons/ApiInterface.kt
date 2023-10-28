package com.avision.commons
import com.onlinetalentsearchexam.model.Login
import com.onlinetalentsearchexam.model.SendDataModel
import com.onlinetalentsearchexam.model.SubmitTest
import com.onlinetalentsearchexam.model.examtest.Examtest
import com.onlinetalentsearchexam.model.savequestion.SaveQus
import com.onlinetalentsearchexam.model.start_test.StartTest
import com.onlinetalentsearchexam.model.viewresult.ViewResult
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST("svclogin")
    fun login(@Body body: RequestBody?): Call<Login>

    @POST("exam_test")
    fun examtest(@Body body: RequestBody?): Call<Examtest>

    @POST("start_test")
    fun start_test(@Body body: RequestBody?): Call<StartTest>

    @POST("save_question")
    fun save_question(@Body body: RequestBody?): Call<SaveQus>

    @POST("submit_test")
    fun submit_test(@Body body: RequestBody?): Call<SubmitTest>
    @POST("submit_test")
    fun submitTest(@Body data: SendDataModel): Call<Void>

    @POST("view_result")
    fun view_result(@Body body: RequestBody?): Call<ViewResult>

}