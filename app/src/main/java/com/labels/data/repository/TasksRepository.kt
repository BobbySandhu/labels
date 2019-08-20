package com.labels.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.labels.http.ApiInterface
import com.labels.model.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TasksRepository {

    private val webservice by lazy {
        Retrofit.Builder()
            .baseUrl("https://ci.edelmansp.co.in/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
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
}