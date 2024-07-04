package com.onlinetalentsearchexam.maharaj.apiservice
import com.onlinetalentsearchexam.maharaj.data.models.CorrectAnsResponse
import com.onlinetalentsearchexam.maharaj.data.models.IntroResponse
import com.onlinetalentsearchexam.maharaj.data.models.QuizAnsSubmitRequest
import com.onlinetalentsearchexam.maharaj.data.models.QuizAnsSubmitResponse
import com.onlinetalentsearchexam.maharaj.data.models.QuizDetailResponse
import com.onlinetalentsearchexam.maharaj.data.models.QuizQuestionResponse
import com.onlinetalentsearchexam.maharaj.data.models.ResultResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PartMap


interface ApiService {

    @GET("instruction")
    suspend fun getIntro(): IntroResponse

    @Multipart
    @POST("exam_test")
    suspend fun getQuizDetail(@PartMap() partMap: MutableMap<String, RequestBody>): QuizDetailResponse

    @Multipart
    @POST("start_test")
    suspend fun getQuestionAnswer(@PartMap() partMap: MutableMap<String, RequestBody>): QuizQuestionResponse

    @Multipart
    @POST("exam_qus_ans")
    suspend fun getCorrectAns(@PartMap() partMap: MutableMap<String, RequestBody>): CorrectAnsResponse

    @Multipart
    @POST("view_result")
    suspend fun getResult(@PartMap() partMap: MutableMap<String, RequestBody>): ResultResponse
    @POST("submit_test")
    fun quizAnsSubmit(@Body data: QuizAnsSubmitRequest): QuizAnsSubmitResponse

}
