package com.onlinetalentsearchexam.model

import com.google.gson.annotations.SerializedName

data class Qusdata (

    @SerializedName("QusID"     ) var QusID     : String? = null,
    @SerializedName("SelAnsID"  ) var SelAnsID  : String? = null,
    @SerializedName("ansA"      ) var ansA      : String? = null,
    @SerializedName("ansB"      ) var ansB      : String? = null,
    @SerializedName("ansC"      ) var ansC      : String? = null,
    @SerializedName("ansD"      ) var ansD      : String? = null,
    @SerializedName("ansTxt"    ) var ansTxt    : String? = null,
    @SerializedName("id"        ) var id        : Int?    = null,
    @SerializedName("noteTitle" ) var noteTitle : String? = null,
    @SerializedName("timeStamp" ) var timeStamp : String? = null



)