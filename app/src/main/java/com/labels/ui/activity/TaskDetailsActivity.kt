package com.labels.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.labels.R
import com.labels.data.viewmodel.TasksDetailsViewModel
import com.labels.model.details.TaskDetailResponse
import com.labels.ui.adapters.TaskDetailAdapter
import com.labels.utils.Constants
import kotlinx.android.synthetic.main.activity_task_details.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class TaskDetailsActivity : AppCompatActivity(), TaskDetailAdapter.ImageCaptureListener {

    private lateinit var taskDetailsViewModel: TasksDetailsViewModel
    private lateinit var taskDetailAdapter: TaskDetailAdapter
    private var currentPhotoPath: String = ""
    private var adapterPosition: Int = 0

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

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.REQUEST_IMAGE_CAPTURE -> {
                    getImageFromCamera()
                }

                Constants.REQUEST_EDITED_IMAGE -> {
                    if (!TextUtils.isEmpty(data!!.getStringExtra(Constants.INTENT_EDITED_IMAGE))) {
                        taskDetailAdapter.updateImageAdapter(adapterPosition, data.getStringExtra(Constants.INTENT_EDITED_IMAGE))
                    }
                }
            }
        }
    }

    private fun getImageFromCamera() {
        if (currentPhotoPath == null) return

        val path = File(currentPhotoPath)
        if (!path.exists()) path.mkdirs()

        if (!TextUtils.isEmpty(currentPhotoPath)) {
            startActivityForResult(Intent(this, EditImageActivity::class.java).putExtra("captured_image", currentPhotoPath), Constants.REQUEST_EDITED_IMAGE)
        }
    }

    override fun onImageCapture(postion: Int) {

        adapterPosition = postion

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }

                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.labels.provider",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }

    private fun getTaskDetails() {
        taskDetailsViewModel.getTasksDetails().observe(this, Observer {
            setRecycler(it)
        })
    }

    private fun setRecycler(taskList: ArrayList<TaskDetailResponse>) {
        taskDetailAdapter = TaskDetailAdapter(taskList)
        recycler_task_details.layoutManager = LinearLayoutManager(this)
        recycler_task_details.adapter = taskDetailAdapter
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
