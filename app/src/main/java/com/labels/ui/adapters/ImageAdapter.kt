package com.labels.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.labels.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_task_image.view.*
import kotlinx.android.synthetic.main.item_task_image_image.view.*

class ImageAdapter(val imageUrls: ArrayList<String>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        context = parent.context
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_task_image_image, parent, false)

        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.setData(imageUrls[position], position)
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.image_task_image
        val imageRemove = itemView.image_task_image_remove

        fun setData(imageUrl: String, position: Int) {

            /*if (imageUrl == "image_close")
                Picasso.get().load(R.drawable.ic_add).into(image)
            else */
            Picasso.get().load(imageUrl).into(image)

            imageRemove.setOnClickListener {
                Toast.makeText(context, "clicked $position", Toast.LENGTH_SHORT).show()
            }


        }
    }
}