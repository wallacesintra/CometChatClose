package com.example.cometchatclose.network

import com.example.cometchatclose.BuildConfig
import com.google.gson.Gson
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

//base url =  https://{appid}.api-{region}.cometchat.io/v3/users/{uid}/auth_tokens



const val APP_ID = BuildConfig.APP_ID
const val region = BuildConfig.REGION
const val REST_API_KEY = BuildConfig.REST_API_KEY


fun getAuthToken(userId: String):CometAuthDto  {
    val client = OkHttpClient()

    val mediaType = "application/json".toMediaTypeOrNull()
    val body = RequestBody.create(mediaType, "{\"force\":true}")
    val request = Request.Builder()
        .url("https://${APP_ID}.api-${region}.cometchat.io/v3/users/${userId}/auth_tokens")
        .post(body)
        .addHeader("accept", "application/json")
        .addHeader("content-type", "application/json")
        .addHeader("apikey", REST_API_KEY)
        .build()

    val response = client.newCall(request).execute()
    return Gson().fromJson(response.body?.string(), CometAuthDto::class.java)
}