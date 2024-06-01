package com.example.cometchatclose.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.cometchat.chat.core.CometChat
import com.cometchat.chat.models.TextMessage
import com.example.cometchatclose.presentation.viewmodels.MessageViewModel

@Composable
fun Home(
    loggedInUser: String,
    messageViewModel: MessageViewModel,
    messageList: List<TextMessage> = emptyList()
){

    var message by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        messageViewModel.receiveMessageWhenOffline(
            userId = loggedInUser,
            latestMessageId = CometChat.getLastDeliveredMessageId()
        )
    }

    Column {
        Text(text = loggedInUser)
        Text(text = CometChat.getLoggedInUser().uid)

        val receiveID = if (loggedInUser == "wallacesintra") "elliotsintra" else "wallacesintra"

        TextField(value =message , onValueChange = {message = it} )

        Button(onClick = {
            messageViewModel.sendMessageToUser(
                receiverId = receiveID,
                message = message
            )

            message = ""
        }) {
            Text(text = "send message")
        }

        LazyColumn {
            items(messageList){message ->
                val sender = if (message.sender == null) "You" else message.sender.uid
                Text(text = "$sender: ${message.text}")
            }
        }
    }
}