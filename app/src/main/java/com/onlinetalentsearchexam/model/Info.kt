package com.onlinetalentsearchexam.model

import com.google.gson.annotations.SerializedName


data class Info (

  @SerializedName("id"           ) var id          : String?                = null,
  @SerializedName("firstname"    ) var firstname   : String?                = null,
  @SerializedName("lastname"     ) var lastname    : String?                = null,
  @SerializedName("role"         ) var role        : String?                = null,
  @SerializedName("class"        ) var cla_ss       : String?                = null,
  @SerializedName("section"      ) var section     : String?                = null,
  @SerializedName("childType"    ) var childType   : String?                = null,
  @SerializedName("student_code" ) var studentCode : ArrayList<StudentCode> = arrayListOf()

)