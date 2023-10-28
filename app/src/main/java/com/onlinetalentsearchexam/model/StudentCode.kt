package com.onlinetalentsearchexam.model

import com.google.gson.annotations.SerializedName


data class StudentCode (

  @SerializedName("student_code"  ) var studentCode : String? = null,
  @SerializedName("First_Name"    ) var FirstName   : String? = null,
  @SerializedName("Last_Name"     ) var LastName    : String? = null,
  @SerializedName("Student_id"    ) var StudentId   : String? = null,
  @SerializedName("usr_rating"    ) var usrRating   : String? = null,
  @SerializedName("Is_active"     ) var IsActive    : String? = null,
  @SerializedName("class_or_year" ) var classOrYear : String? = null,
  @SerializedName("section"       ) var section     : String? = null

)