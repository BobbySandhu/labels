package com.labels.ui.fragments.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.labels.R
import com.labels.data.viewmodel.TasksDetailsViewModel
import com.labels.ui.adapters.TaskDetailAdapter
import kotlinx.android.synthetic.main.activity_task_details.*

class TaskDetailsActivity : AppCompatActivity() {

    private lateinit var taskDetailsViewModel: TasksDetailsViewModel
    private lateinit var taskDetailAdapter: TaskDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_task_detail)

        taskDetailsViewModel = ViewModelProviders.of(this).get(TasksDetailsViewModel::class.java)
        getTaskDetails()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getTaskDetails() {
        taskDetailsViewModel.getTasksDetails().observe(this, Observer {
            taskDetailAdapter = TaskDetailAdapter(it)
            recycler_task_details.layoutManager = LinearLayoutManager(this)
            recycler_task_details.adapter = taskDetailAdapter
        })
    }
}
