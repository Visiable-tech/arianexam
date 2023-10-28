package com.onlinetalentsearchexam.model.savequestion

import com.google.gson.annotations.SerializedName


data class SaveQus (

    @SerializedName("status"  ) var status  : String?  = null,
    @SerializedName("message" ) var message : Boolean? = null

)