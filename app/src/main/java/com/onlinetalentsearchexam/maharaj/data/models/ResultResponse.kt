package com.onlinetalentsearchexam.maharaj.data.models

data class ResultResponse (
    val status: String? = null,
    val message: String? = null,
    val data: Result? = null
)

data class Result (
    val correct: Int? = null,
    val incorrect: Int? = null,
    val exam_id: String? = null
)
