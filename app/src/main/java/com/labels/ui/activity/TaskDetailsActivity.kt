package com.labels.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.labels.R
import com.labels.data.viewmodel.TasksDetailsViewModel
import com.labels.ui.adapters.TaskDetailAdapter
import com.labels.utils.Constants
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

        setSubmitClick()
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            Constants.REQUEST_CAMERA -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("aaa", "Permission has been denied by user")

                } else {
                    Log.i("aaa", "Permission has been granted by user")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.extras != null) {
                val imageBitmap = data.extras!!.get("data") as Bitmap
            }
        }
    }

    private fun getTaskDetails() {
        taskDetailsViewModel.getTasksDetails().observe(this, Observer {
            taskDetailAdapter = TaskDetailAdapter(it)
            recycler_task_details.layoutManager = LinearLayoutManager(this)
            recycler_task_details.adapter = taskDetailAdapter
        })
    }

    private fun setSubmitClick() {
        button_submit_task.setOnClickListener {
            if (taskDetailAdapter == null) return@setOnClickListener

            val map = taskDetailAdapter.isAnAnswerEmpty()

            for ((key, value) in map) {
                if (!value) { //TODO extract hardcoded string
                    Toast.makeText(this, "Please fill all the answers", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val allAnswerMap = taskDetailAdapter.getAllAnswerMap()
            val gson = Gson()
            val answerString = gson.toJson(allAnswerMap)

            Log.d("bbb", answerString)
        }
    }

    private fun sendAnswers(answerJsonString: String) {
        taskDetailsViewModel.sendAnswers(answerJsonString).observe(this, Observer {
            Log.d("aaa", it)
        })
    }
}
