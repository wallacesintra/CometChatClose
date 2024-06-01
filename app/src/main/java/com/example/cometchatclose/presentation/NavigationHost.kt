package com.example.cometchatclose.presentation

import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cometchat.chat.core.CometChat
import com.cometchat.chat.models.TextMessage
import com.example.cometchatclose.presentation.LogInScreen
import com.example.cometchatclose.presentation.viewmodels.MessageViewModel
import com.example.cometchatclose.presentation.viewmodels.UserViewModel

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel,
    messageViewModel: MessageViewModel,
    messageList: List<TextMessage>
){
    val navController = rememberNavController()



    NavHost(
        navController =navController,
        startDestination = Screens.LogIn.route,
        modifier = Modifier.padding(20.dp)
    ) {

        composable(Screens.LogIn.route){
            LogInScreen(useIDInput = "", loginEvent = {userIdInput ->

//                if (userViewModel.logInUser(userIdInput))
                userViewModel.logInUser(userIdInput)

                if(CometChat.getLoggedInUser() != null){
                    navController.navigate(Screens.Home.createRoute(
                        userId = userIdInput
                    ))
                }

            })
        }

        composable(
            Screens.Home.route,
            arguments = Screens.Home.navArguments
        ) {

            val userId = it.arguments?.getString("userId")

            if (userId != null) {
                Home(loggedInUser = userId, messageViewModel = messageViewModel, messageList = messageList )
            }
        }

    }
}