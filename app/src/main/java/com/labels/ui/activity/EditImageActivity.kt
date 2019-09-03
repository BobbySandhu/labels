package com.labels.ui.activity

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.labels.R
import com.labels.utils.Constants
import com.labels.utils.Utils
import kotlinx.android.synthetic.main.activity_edit_image.*
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.channels.FileChannel

class EditImageActivity : AppCompatActivity() {

    private lateinit var imagePath: String
    private var bmp: Bitmap? = null
    private var alteredBitmap: Bitmap? = null
    private var imageFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_image)

        getIntentData()
        setSaveButtonListener()
    }

    private fun getIntentData() {
        imagePath = intent.getStringExtra("captured_image")

        displayImageToEdit()
    }


    private fun displayImageToEdit() {
        try {
            val imageResolution = 800
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

            //if (!alteredBitmap.isMutable())
            //   alteredBitmap = convertToMutable(alteredBitmap)

            image_edit.setNewImage(alteredBitmap, bmp)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setSaveButtonListener() {
        button_upload.setOnClickListener {
            if (alteredBitmap != null && imageFileUri == null) {
                val contentValues = ContentValues(3)
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "Draw here")

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


    fun convertToMutable(imgIn: Bitmap): Bitmap {
        var imgIn = imgIn
        try {
            val file = File(Environment.getExternalStorageDirectory().toString() + File.separator + "temp.tmp")
            val randomAccessFile = RandomAccessFile(file, "rw")

            val width = imgIn.width
            val height = imgIn.height
            val type = imgIn.config

            val channel = randomAccessFile.getChannel()
            val map = channel.map(FileChannel.MapMode.READ_WRITE, 0, (imgIn.rowBytes * height).toLong())
            imgIn.copyPixelsToBuffer(map)

            System.gc()

            imgIn = Bitmap.createBitmap(width, height, type)
            map.position(0)
            imgIn.copyPixelsFromBuffer(map)
            channel.close()
            randomAccessFile.close()

            file.delete()

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return imgIn
    }
}
