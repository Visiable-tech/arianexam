package com.onlinetalentsearchexam.maharaj.data.models



data class QuizResultSubmitRequest(
    var correct: Int = 0,
    var incorrect: Int = 0,
    var student_id:String?=null,
    var exam_id:String?=null,
    var exam_taken_id:String?=null
)
