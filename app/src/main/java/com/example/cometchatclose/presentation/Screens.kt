package com.example.cometchatclose.presentation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screens(val route: String, val navArguments: List<NamedNavArgument> = emptyList()){

    data object LogIn: Screens("logIn")
    data object Home: Screens(
        "Home/{userId}",
        navArguments = listOf(navArgument("userId"){
            type = NavType.StringType
        })
    ){
        fun createRoute(userId: String) = "Home/${userId}"
    }

}