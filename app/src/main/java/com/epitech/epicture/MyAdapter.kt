package com.epitech.epicture

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class MyAdaptater(val context: Context, private val list: ArrayList<DataImage>) : RecyclerView.Adapter<MyAdaptater.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder = ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_galery, p0, false))

   override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(picture: ViewHolder, nextPicture: Int) {
        Picasso.with(context).load(list[nextPicture].url).into(picture.imageViewUrl)
        picture.titleImage.text = list[nextPicture].title
        picture.descriptionImage.text = list[nextPicture].description
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewUrl = itemView.findViewById(R.id.galleryImg) as ImageView
        val titleImage = itemView.findViewById(R.id.title) as TextView
        val descriptionImage = itemView.findViewById(R.id.description) as TextView
    }
}