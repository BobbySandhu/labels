package com.labels.http

import com.labels.model.Task
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("tasks")
    fun getTasks(): Call<Task>
}