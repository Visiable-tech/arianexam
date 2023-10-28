package com.onlinetalentsearchexam.model.start_test

import com.google.gson.annotations.SerializedName


data class AnsArr (

  @SerializedName("answer_id" ) var answerId : String? = null,
  @SerializedName("answer"    ) var answer   : String? = null

)