package com.labels.http

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    val webservice by lazy {
        Retrofit.Builder()
            .baseUrl("https://ci.edelmansp.co.in/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(ApiInterface::class.java)
    }
}