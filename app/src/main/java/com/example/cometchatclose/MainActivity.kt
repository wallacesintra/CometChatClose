package com.example.cometchatclose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cometchat.chat.core.CometChat
import com.cometchat.chat.exceptions.CometChatException
import com.cometchat.chat.models.User
import com.example.cometchatclose.ui.theme.CometChatCloseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)



        val authKey = ""

        val currentUser = User()

        currentUser.uid = ""
        currentUser.name = ""

        CometChat.createUser(currentUser, authKey, object : CometChat.CallbackListener<User>() {
            override fun onSuccess(user: User) {
                Log.d("createUser", user.toString())
            }

            override fun onError(e: CometChatException){
                e.message?.let { Log.e("createUser", it) }
            }
        }
        )

        enableEdgeToEdge()
        setContent {
            CometChatCloseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
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