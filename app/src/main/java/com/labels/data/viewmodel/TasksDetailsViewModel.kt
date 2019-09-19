package com.labels.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.labels.data.repository.TasksRepository
import com.labels.model.Task
import com.labels.model.details.TaskDetailResponse

class TasksDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : TasksRepository = TasksRepository()

    fun getTasksDetails() : LiveData<ArrayList<TaskDetailResponse>> {
        return repository.getTaskDetail()
    }

    fun sendAnswers(answerJson: String) : LiveData<String> {
        return repository.sendAnswers(answerJson)
    }
}