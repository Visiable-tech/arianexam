package com.onlinetalentsearchexam.maharaj.data.models

data class QuizDetail(
    val exam_id: String? = null,
    val exam_name: String? = null,
    val class_or_year: String? = null,
    val tot_time: String? = null,
    val tot_qus: String? = null,
    val subject_name: String? = null,
    val exam_date: String? = null,
    val max_marks: String? = null,
    val instruction: String? = null,
    val logo: String? = null,
    val status: String? = null,
    val status_text: String? = null,
    val exam_taken_id: String? = null
)

data class QuizDetailResponse(
    val status: String? = null,
    val message: List<QuizDetail> ?= null
)