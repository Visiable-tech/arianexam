package com.onlinetalentsearchexam.maharaj.data.models

data class IntroResponse (
    val status: String? = null,
    val message: Message? = null
)

data class Message (
    val title: String? = null,
    val content: String? = null
)