package com.example.cometchatclose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cometchat.chat.core.AppSettings
import com.cometchat.chat.core.CometChat
import com.cometchat.chat.exceptions.CometChatException
import com.example.cometchatclose.presentation.NavigationHost
import com.example.cometchatclose.presentation.viewmodels.MessageViewModel
import com.example.cometchatclose.presentation.viewmodels.UserViewModel
import com.example.cometchatclose.ui.theme.CometChatCloseTheme

class MainActivity : ComponentActivity() {

    //initialise comet chat
    private val appID = BuildConfig.APP_ID
    private val region = BuildConfig.REGION

    private val messageViewModel: MessageViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()



    private val appSetting = AppSettings.AppSettingsBuilder()
        .subscribePresenceForAllUsers()
        .setRegion(region)
        .autoEstablishSocketConnection(true)
        .build()





    override fun onCreate(savedInstanceState: Bundle?) {

        CometChat.init(
            this,
            appID,
            appSetting,
            object : CometChat.CallbackListener<String>() {
                override fun onSuccess(p0: String?) {
                    Log.d("Initialization", "Initialization successful")
                }

                override fun onError(p0: CometChatException?) {
                    Log.e("Initialization", "Initialization failed with exception: " + p0?.message)
                }
            }
        )

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CometChatCloseTheme {
                val messagesState by messageViewModel.messagesState.collectAsState()
                val messageFlow by messageViewModel.messageFLow.collectAsState()
                val message = messagesState.messages
//                val message = messageFlow.messages

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationHost(
                        modifier = Modifier.padding(innerPadding),
                        userViewModel = userViewModel,
                        messageViewModel = messageViewModel,
                        messageList = message
                    )
                }

            }
        }
    }

    override fun onResume() {
        super.onResume()

        messageViewModel.receiveMessageWhenOnline("MainActivity")

    }

    override fun onPause() {
        super.onPause()

        CometChat.removeMessageListener("MainActivity")

        CometChat.logout(object : CometChat.CallbackListener<String>() {
            override fun onSuccess(logoutSuccessMessage: String) {
                Log.d("Logout", "Logout completed successfully")
            }

            override fun onError(e: CometChatException) {
                Log.e("Logout", "Logout failed with exception: " + e.message)
            }
        })
    }

    override fun onStop() {
        super.onStop()

        CometChat.logout(object : CometChat.CallbackListener<String>() {
            override fun onSuccess(logoutSuccessMessage: String) {
                Log.d("Logout", "Logout completed successfully")
            }

            override fun onError(e: CometChatException) {
                Log.e("Logout", "Logout failed with exception: " + e.message)
            }
        })
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CometChatCloseTheme {
        Greeting("Android")
    }
}