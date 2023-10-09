package com.arianinstitute.model.examtest

import com.google.gson.annotations.SerializedName


data class Examtest (

  @SerializedName("status"  ) var status  : String?            = null,
  @SerializedName("message" ) var message : ArrayList<Message> = arrayListOf()

)