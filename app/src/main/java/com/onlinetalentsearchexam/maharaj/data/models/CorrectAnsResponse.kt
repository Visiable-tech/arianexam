package com.onlinetalentsearchexam.maharaj.data.models

data class CorrectAnsResponse (
    val status: String? = null,
    val message: List<QnA>? = null
)

data class QnA (
    val question: String? = null,
    val question_desc: Any? = null,
    val ans_arr: List<Ans>? = null
)

data class Ans (
    val answer: String? = null,
    val ans_status: Int? = null
)
