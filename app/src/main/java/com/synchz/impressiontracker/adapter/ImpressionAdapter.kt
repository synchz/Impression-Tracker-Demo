package com.synchz.impressiontracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.synchz.impressiontracker.R

class ImpressionAdapter(private val listOfPhotos: ArrayList<String>) :
    RecyclerView.Adapter<ImpressionAdapter.ImpressionHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ImpressionHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_impression, parent, false)
    )

    override fun getItemCount() = listOfPhotos.size

    override fun onBindViewHolder(holder: ImpressionHolder, position: Int) =
        holder.bind(listOfPhotos[position])

    class ImpressionHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private var imageView: ImageView = view.findViewById(R.id.imageView)

        fun bind(url: String) {
            view.tag = url
            Glide.with(imageView.context).load(url).into(imageView)
        }
    }
}
