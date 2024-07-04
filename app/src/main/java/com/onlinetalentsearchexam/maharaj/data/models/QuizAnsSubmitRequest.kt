package com.onlinetalentsearchexam.maharaj.data.models



data class QuizAnsSubmitRequest(
    val Details: List<DetailsItem?>,
    val Qusdata: List<QusdataItem?>
)

data class DetailsItem(
    val student_id: Int,
    val test_taken_id: String
)

data class QusdataItem(
    val QusID: String,
    val SelAnsID: String,
)