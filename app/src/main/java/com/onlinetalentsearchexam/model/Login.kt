package com.onlinetalentsearchexam.model

import com.google.gson.annotations.SerializedName


data class Login (

  @SerializedName("status"  ) var status  : String? = null,
  @SerializedName("message" ) var message : String? = null,
  @SerializedName("info"    ) var info    : Info?   = Info()

)