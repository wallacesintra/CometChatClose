package com.example.cometchatclose.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cometchat.chat.core.CometChat
import com.cometchat.chat.exceptions.CometChatException
import com.cometchat.chat.models.User
import com.example.cometchatclose.network.getAuthToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel: ViewModel(){


    fun logInUser(userId: String){
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("LogIn", "login button clicked")
            if (CometChat.getLoggedInUser() == null) {
                CometChat.login(
                    getAuthToken(userId).data.authToken,
                    object : CometChat.CallbackListener<User>() {
                        override fun onSuccess(p0: User?) {
                            Log.d("LogIn", "Login Successful : " + p0?.toString())
                        }

                        override fun onError(p0: CometChatException?) {
                            Log.e("LogIn", "Login failed with exception : " + p0?.message)

                        }
                    }
                )
            }else{
                Log.d("LogIn", "user logged already")
            }
        }
    }
}