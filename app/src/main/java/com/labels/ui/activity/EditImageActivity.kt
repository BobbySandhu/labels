package com.labels.ui.activity

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.labels.R
import com.labels.interfaces.MarkingListener
import com.labels.interfaces.UndoListener
import com.labels.model.MarkingRect
import com.labels.ui.adapters.RecyclerBoxesAdapter
import com.labels.utils.Constants
import com.labels.utils.Utils
import kotlinx.android.synthetic.main.activity_edit_image.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class EditImageActivity : AppCompatActivity(), MarkingListener, RecyclerBoxesAdapter.RemoveMarkingListener {

    override fun onRemove(position: Int) {
        mUndoListener?.onUndo(position)
        Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show()
    }

    override fun onMarking(markedRectsArray: ArrayList<MarkingRect>?) {
        setMarkedRectAdapter(markedRectsArray)
        Toast.makeText(this, "Marked noted", Toast.LENGTH_SHORT).show()
    }

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

    private fun setMarkedRectAdapter(markedRectsArray: ArrayList<MarkingRect>?) {
        if (markedRectsArray != null) {
            val recyclerBoxesAdapter = markedRectsArray?.let { RecyclerBoxesAdapter(it) }
            var layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recycler_boxes.layoutManager = layoutManager
            recycler_boxes.scrollToPosition(markedRectsArray.size - 1)
            recycler_boxes.adapter = recyclerBoxesAdapter
        }
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
        image_save.setOnClickListener {
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
            //mUndoListener?.onUndo()
        }
    }
}
