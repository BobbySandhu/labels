package com.labels.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.labels.R
import com.labels.data.viewmodel.TasksViewModel
import com.labels.model.Task
import com.labels.ui.adapters.TaskListAdapter
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {

    private lateinit var taskViewModel : TasksViewModel
    private lateinit var taskListAdapter: TaskListAdapter

    companion object {
        @JvmStatic
        fun newInstance() = DashboardFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        taskViewModel = ViewModelProviders.of(this).get(TasksViewModel::class.java)

        getSurvey()

        return view
    }

    private fun getSurvey() {
        taskViewModel.getTasks().observe(this, Observer {
            //TODO set recycler here
            Log.d("aaa ", it.toString())

            if (it != null)
                card_no_survey.visibility = GONE
            else card_no_survey.visibility = VISIBLE

            setRecycler(it)
        })
    }

    private fun setRecycler(task: Task) {
        taskListAdapter = TaskListAdapter(task)
        recycler_tasks.layoutManager = LinearLayoutManager(activity)
        recycler_tasks.adapter = taskListAdapter
    }
}
