package com.example.cometchatclose.model

import com.cometchat.chat.models.TextMessage

data class MessageFlow(
    val messageList: List<TextMessage> = emptyList()
)