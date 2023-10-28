package com.onlinetalentsearchexam.model.start_test

import com.google.gson.annotations.SerializedName


data class Message (

  @SerializedName("exam_taken_id" ) var examTakenId  : Int?              = null,
  @SerializedName("question_id"   ) var questionId   : String?           = null,
  @SerializedName("question"      ) var question     : String?           = null,
  @SerializedName("question_desc" ) var questionDesc : String?           = null,
  @SerializedName("ans_arr"       ) var ansArr       : ArrayList<AnsArr> = arrayListOf()

)