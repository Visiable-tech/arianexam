package com.arianinstitute.model

import com.google.gson.annotations.SerializedName

data class Details (

    @SerializedName("student_id"    ) var studentId   : Int? = null,
    @SerializedName("test_taken_id" ) var testTakenId : Int? = null

)