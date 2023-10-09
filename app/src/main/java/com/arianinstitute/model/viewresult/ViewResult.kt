package com.arianinstitute.model.viewresult

import com.google.gson.annotations.SerializedName


data class ViewResult (

    @SerializedName("status"  ) var status  : String?  = null,
    @SerializedName("message" ) var message : Message? = Message()

)