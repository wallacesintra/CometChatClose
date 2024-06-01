package com.example.cometchatclose.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun LogInScreen(
    useIDInput: String,
    loginEvent: (String) -> Unit = {}
){
    var userID by remember {
        mutableStateOf(useIDInput)
    }

    Column {
        TextField(
            value = userID,
            onValueChange = {
                userID = it
            }
        )

        Button(onClick = { loginEvent(userID) }) {
            Text(text = "log in")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLoginScreen(){
    LogInScreen(useIDInput = "")
}