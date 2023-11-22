package com.onlinetalentsearchexam.maharaj.retrofit.exception


data class ErrorBody (
    var code: String? = null,
    var error: String? = null,
    var message: String? = null,
    var correlationId: String? = null
)