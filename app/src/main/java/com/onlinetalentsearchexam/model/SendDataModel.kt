package com.onlinetalentsearchexam.model

data class DetailsItem(val student_id: Int,
                       val test_taken_id: String
)

data class QusdataItem(
    val QusID: String,
    val SelAnsID: String,
//    val ansA: String,
//    val ansB: String,
//    val ansC: String,
//    val ansD: String,
//    val ansTxt: String,
//    val id: Int,
//    val noteTitle: String,
//    val timeStamp: String
)

data class SendDataModel(
    val Details: List<DetailsItem?>,
    val Qusdata: List<QusdataItem?>
)