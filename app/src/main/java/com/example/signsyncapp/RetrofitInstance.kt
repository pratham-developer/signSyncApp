package com.example.signsyncapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: ApiService by lazy {
        createRetrofit()
    }

    private fun createRetrofit(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://signsyncbackend.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
