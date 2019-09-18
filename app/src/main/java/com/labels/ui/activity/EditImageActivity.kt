package com.labels.ui.activity

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.labels.R
import com.labels.utils.Constants
import com.labels.utils.Utils
import kotlinx.android.synthetic.main.activity_edit_image.*
import java.io.File

class EditImageActivity : AppCompatActivity() {

    private lateinit var imagePath: String
    private var bmp: Bitmap? = null
    private var alteredBitmap: Bitmap? = null
    private var imageFileUri: Uri? = null
    private var mUndoListener: UndoListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_image)

        getIntentData()
        setSaveButtonListener()
        setUndoButtonListener()
    }

    private fun getIntentData() {
        imagePath = intent.getStringExtra("captured_image") //TODO extract constant

        displayImageToEdit()
    }


    private fun displayImageToEdit() {
        try {
            val imageResolution = 1000
            val bmpFactoryOptions = BitmapFactory.Options()
            bmpFactoryOptions.inJustDecodeBounds = true

            bmp = BitmapFactory.decodeStream(
                    contentResolver.openInputStream(Uri.fromFile(File(imagePath))),
                    null,
                    bmpFactoryOptions
            )

            bmpFactoryOptions.inSampleSize =
                    Utils.calculateInSampleSize(bmpFactoryOptions, imageResolution, imageResolution)
            bmpFactoryOptions.inJustDecodeBounds = false

            bmp = BitmapFactory.decodeStream(
                    contentResolver.openInputStream(Uri.fromFile(File(imagePath))),
                    null,
                    bmpFactoryOptions
            )

            alteredBitmap = Utils.getResizedBitmap(bmp, imageResolution)
            bmp = Utils.getResizedBitmap(bmp, imageResolution)

            image_edit.setNewImage(this, alteredBitmap, bmp)
            mUndoListener = image_edit.instance
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setSaveButtonListener() {
        button_upload.setOnClickListener {
            if (alteredBitmap != null && imageFileUri == null) {
                val contentValues = ContentValues(3)
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "labels-image")

                imageFileUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                try {
                    val imageFileOS = contentResolver.openOutputStream(imageFileUri)
                    alteredBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, imageFileOS)
                    val t = Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT)
                    t.show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            if (imageFileUri != null) {
                //TODO upload to server code
                val intent = Intent()
                intent.putExtra(Constants.INTENT_EDITED_IMAGE, imagePath) //TODO manage cancel editing value
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun setUndoButtonListener() {
        image_undo.setOnClickListener {
            mUndoListener?.onUndo()
        }
    }

    interface UndoListener {
        fun onUndo()
    }
}
