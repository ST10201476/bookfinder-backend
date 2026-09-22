package com.example.bookfinder.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MyBackendClient {

    private const val BASE_URL = "https://bookfinder-backend-production-a2a4.up.railway.app/"

    val api: MyBackendApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyBackendApi::class.java)
    }
}