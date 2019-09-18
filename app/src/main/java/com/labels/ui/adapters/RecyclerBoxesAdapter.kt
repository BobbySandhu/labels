package com.labels.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.labels.R
import com.labels.model.MarkingRect
import kotlinx.android.synthetic.main.item_marked_boxes.view.*

class RecyclerBoxesAdapter(var markedRects: ArrayList<MarkingRect>) : RecyclerView.Adapter<RecyclerBoxesAdapter.MyViewHolder>() {

    private lateinit var removeMarkingListener: RemoveMarkingListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        removeMarkingListener = parent.context as RemoveMarkingListener
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_marked_boxes, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return markedRects.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(markedRects[position], position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageRect = itemView.view_rect
        val imageRemove = itemView.image_remove

        fun setData(rect: MarkingRect, position: Int) {
            imageRect.setBackgroundColor(rect.rectColor)
            imageRemove.setOnClickListener {
                removeMarkingListener.onRemove(position)
            }
        }
    }

    interface RemoveMarkingListener {
        fun onRemove(position: Int)
    }
}