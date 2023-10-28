package com.onlinetalentsearchexam.model.start_test

import com.google.gson.annotations.SerializedName


data class StartTest (

  @SerializedName("status"  ) var status  : String?            = null,
  @SerializedName("message" ) var message : ArrayList<Message> = arrayListOf()

)