package com.onlinetalentsearchexam.model.viewresult

import com.google.gson.annotations.SerializedName


data class Message (

    @SerializedName("correct"   ) var correct   : Int?    = null,
    @SerializedName("incorrect" ) var incorrect : Int?    = null,
    @SerializedName("exam_id"   ) var examId    : String? = null

)