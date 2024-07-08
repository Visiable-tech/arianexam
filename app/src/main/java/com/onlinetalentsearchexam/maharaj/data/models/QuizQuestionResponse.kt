package com.onlinetalentsearchexam.maharaj.data.models

data class QuizQuestionResponse (
    var status: String? = null,
    var message: List<Question>? = null
)

data class Question (
    var exam_taken_id: Long? = null,
    var question_id: String? = null,
    var question: String? = null,
    var question_desc: Any? = null,
    var ans_arr: List<Answers>? = null,
    var selectedAnsId:String="0",  //not in api
    var selectedAns:String=""  //not in api

)

data class Answers (
    var ans_status: String?=null,
    var answer_id: String? = null,
    var answer: String? = null
)
