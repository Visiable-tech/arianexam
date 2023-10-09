package com.arianinstitute.model.examtest

import com.google.gson.annotations.SerializedName


data class Message (

  @SerializedName("exam_id"       ) var examId      : String? = null,
  @SerializedName("exam_name"     ) var examName    : String? = null,
  @SerializedName("class_or_year" ) var classOrYear : String? = null,
  @SerializedName("tot_time"      ) var totTime     : String? = null,
  @SerializedName("tot_qus"       ) var totQus      : String? = null,
  @SerializedName("subject_name"  ) var subjectName : String? = null,
  @SerializedName("exam_date"     ) var examDate    : String? = null,
  @SerializedName("max_marks"     ) var maxMarks    : String? = null,
  @SerializedName("instruction"   ) var instruction : String? = null,
  @SerializedName("logo"          ) var logo        : String? = null,
  @SerializedName("status"        ) var status      : Int?    = null,
  @SerializedName("status_text"   ) var statusText  : String? = null,
  @SerializedName("exam_taken_id" ) var examTakenId : Int?    = null

)