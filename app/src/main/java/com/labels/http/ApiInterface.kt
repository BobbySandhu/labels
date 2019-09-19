package com.labels.http

import com.labels.model.Task
import com.labels.model.details.TaskDetailResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {

    @GET("tasks")
    fun getTasks(): Call<Task>

    @GET("tasks/task-detail/1")
    fun getTaskDetail(): Call<ArrayList<TaskDetailResponse>>

    @POST("tasks/task-details")
    fun sendAnswers(@Body answerJson: String): Call<String>
}