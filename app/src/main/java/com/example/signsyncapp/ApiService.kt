package com.example.signsyncapp

import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("/user/login")
    suspend fun loginToBackend(
        @Header("Authorization") authToken: String
    ): Response<Resp>

    @GET("/user")
    suspend fun getUser(
        @Header("Authorization") authToken: String,
    ): Response<Resp>

}


