package com.example.cometchatclose.model

import com.cometchat.chat.models.TextMessage

data class MessagesState(
    val messages: List<TextMessage> = emptyList()
)