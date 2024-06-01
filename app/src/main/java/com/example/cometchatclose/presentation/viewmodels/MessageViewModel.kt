package com.example.cometchatclose.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cometchat.chat.constants.CometChatConstants
import com.cometchat.chat.core.CometChat
import com.cometchat.chat.core.CometChat.CallbackListener
import com.cometchat.chat.core.CometChat.MessageListener
import com.cometchat.chat.core.MessagesRequest.MessagesRequestBuilder
import com.cometchat.chat.exceptions.CometChatException
import com.cometchat.chat.models.BaseMessage
import com.cometchat.chat.models.TextMessage
import com.example.cometchatclose.model.MessagesState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MessageViewModel: ViewModel() {

    private val _messageState = MutableStateFlow(MessagesState())
    val messagesState: StateFlow<MessagesState> = _messageState.asStateFlow()

    var messageList: MutableList<TextMessage> = mutableListOf()

    val messageFLow: StateFlow<MessagesState> = combine(_messageState, messageList.asFlow()) { state, messages ->
        state.copy(
            messages = listOf(messages)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MessagesState())





    fun sendMessageToUser(receiverId: String, message: String){
        val receiverType = CometChatConstants.RECEIVER_TYPE_USER

        val textMessage = TextMessage(
            receiverId,
            message,
            receiverType
        )
        viewModelScope.launch {
            CometChat.sendMessage(textMessage, object: CallbackListener<TextMessage>(){
                override fun onSuccess(p0: TextMessage?) {
                    Log.d("Message Sent", "Message sent successfully: $p0")
                }

                override fun onError(p0: CometChatException?) {
                    Log.e("Message Sent", "message failed with exception: " + p0?.message)
                }
            })

            messageList.add(textMessage)

            _messageState.update {
                it.copy(
                    messages = messageList
                )
            }
        }
    }

    fun receiveMessageWhenOffline(userId:String, latestMessageId: Int){

        val messageRequest = MessagesRequestBuilder()
            .setMessageId(latestMessageId)
            .setLimit(60)
            .setUID(userId)
            .build()

        viewModelScope.launch(Dispatchers.IO) {
            messageRequest.fetchPrevious(object : CallbackListener<List<BaseMessage?>>(){
                override fun onSuccess(list: List<BaseMessage?>?) {
                    if (list != null) {
                        for (message in list){
                            if (message is TextMessage){
                                Log.d("Message received", "text message received successfully: $message")

                                messageList.add(message)
                            }
                        }

                        _messageState.update {
                            it.copy(
                                messages = messageList
                            )
                        }
                    }
                }

                override fun onError(e: CometChatException?) {
                    Log.e("Message received", "message fetched with exception: " + e?.message)
                }
            })
        }
    }

    fun receiveMessageWhenOnline(listenerID: String){
        viewModelScope.launch(Dispatchers.IO) {
            CometChat.addMessageListener(listenerID, object: MessageListener(){
                override fun onTextMessageReceived(textMessage: TextMessage?) {
//                super.onTextMessageReceived(textMessage)
                    if (textMessage != null) {
                        Log.d("Message received online", "message received successfully: $textMessage")
                        messageList.add(textMessage)

                        _messageState.update {
                            it.copy(
                                messages = messageList
                            )
                        }
                    }
                }
            })
        }


    }
}