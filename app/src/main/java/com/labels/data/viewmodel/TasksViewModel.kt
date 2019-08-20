package com.labels.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.labels.data.repository.TasksRepository
import com.labels.model.Task

class TasksViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : TasksRepository = TasksRepository()

    fun getTasks() : LiveData<Task> {
        return repository.getTasks()
    }
}