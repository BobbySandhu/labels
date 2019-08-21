package com.labels.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.labels.R
import com.labels.model.details.TaskDetailResponse
import kotlinx.android.synthetic.main.item_task_audio.view.*
import kotlinx.android.synthetic.main.item_task_image.view.*
import kotlinx.android.synthetic.main.item_task_mcq_checkbox.view.*
import kotlinx.android.synthetic.main.item_task_mcq_radio.view.*

class TaskDetailAdapter(val taskDetail: ArrayList<TaskDetailResponse>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_IMAGE = "image"
        const val TYPE_AUDIO = "audio"
        const val TYPE_MCQ_RADIO = "mcq_radio"
        const val TYPE_MCQ_CHECKBOX = "mcq_checkbox"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> ImageHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_task_image, parent, false))
            1 -> AudioHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_task_audio, parent, false))
            2 -> McqRadioHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_task_mcq_radio, parent, false))
            3 -> McqCheckboxHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_task_mcq_checkbox, parent, false))
            else -> McqRadioHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_task_mcq_radio, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return taskDetail.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (taskDetail[position].type) {
            TYPE_IMAGE -> (holder as ImageHolder).setData(taskDetail[position])
            TYPE_AUDIO -> (holder as AudioHolder).setData(taskDetail[position])
            TYPE_MCQ_RADIO -> (holder as McqRadioHolder).setData(taskDetail[position])
            TYPE_MCQ_CHECKBOX -> (holder as McqCheckboxHolder).setData(taskDetail[position])
            else -> (holder as McqRadioHolder).setData(taskDetail[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (taskDetail[position].type) {
            TYPE_IMAGE -> 0
            TYPE_AUDIO -> 1
            TYPE_MCQ_RADIO -> 2
            TYPE_MCQ_CHECKBOX -> 3
            else -> 2
        }
    }

    inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val question = itemView.text_image_question

        fun setData(taskDetail: TaskDetailResponse) {
            question.text = taskDetail.question
        }
    }

    inner class AudioHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val question = itemView.text_audio_question

        fun setData(taskDetail: TaskDetailResponse) {
            question.text = taskDetail.question
        }
    }

    inner class McqRadioHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val question = itemView.text_mcq_radio

        fun setData(taskDetail: TaskDetailResponse) {
            question.text = taskDetail.question
        }
    }

    inner class McqCheckboxHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val question = itemView.text_mcq_checkbox

        fun setData(taskDetail: TaskDetailResponse) {
            question.text = taskDetail.question
        }
    }
}
