package com.labels.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.labels.http.ApiInterface
import com.labels.model.Task
import com.labels.model.details.TaskDetailResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TasksRepository {

    private val webservice by lazy {
        val interceptor = HttpLoggingInterceptor()
        val client = OkHttpClient().newBuilder().addInterceptor(interceptor).build()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        Retrofit.Builder()
            //.baseUrl("https://ci.edelmansp.co.in/")
            .baseUrl("http://54.243.149.109:7000/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(client)
            .build().create(ApiInterface::class.java)
    }

    fun getTasks(): LiveData<Task> {
        val tasksLiveData = MutableLiveData<Task>()

        webservice.getTasks().enqueue(object : Callback<Task> {
            override fun onFailure(call: Call<Task>, t: Throwable) {
                Log.d("aaa", t.toString())
            }

            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                tasksLiveData.value = response.body()
                Log.d("aaa", response.toString())
            }
        })

        return tasksLiveData
    }

    fun getTaskDetail(): LiveData<ArrayList<TaskDetailResponse>> {
        val taskDetailLiveData = MutableLiveData<ArrayList<TaskDetailResponse>>()
        webservice.getTaskDetail().enqueue(object: Callback<ArrayList<TaskDetailResponse>> {
            override fun onFailure(call: Call<ArrayList<TaskDetailResponse>>, t: Throwable) {
                Log.d("aaa", t.toString())
            }

            override fun onResponse(call: Call<ArrayList<TaskDetailResponse>>, response: Response<ArrayList<TaskDetailResponse>>) {
                taskDetailLiveData.value = response.body()
                Log.d("aaa", response.toString())
            }
        })

        return taskDetailLiveData
    }

    fun sendAnswers(answerJson: String): LiveData<String> {
        val answerResponseLiveData = MutableLiveData<String>()
        webservice.sendAnswers(answerJson).enqueue(object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("aaa", t.toString())
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                answerResponseLiveData.value = response.body()
                Log.d("aaa", response.toString())
            }
        })

        return answerResponseLiveData
    }
}