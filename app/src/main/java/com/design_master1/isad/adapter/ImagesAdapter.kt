package com.design_master1.isad.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.design_master1.isad.databinding.ItemImagesBinding
import com.design_master1.isad.model.network.response.FetchLocationResponseClasses
import com.design_master1.isad.utils.helper.Helper

class ImagesAdapter: RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {
    var images = mutableListOf<FetchLocationResponseClasses.Image>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemImagesBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Helper.loadImage(
            url = "event_location/${images[position].imageUrl}",
            isCompleteURL = false,
            imageView = holder.binding.image,
            listener = object: Helper.LoadImageListener{
                override fun onImageLoaded() {}
                override fun onFailedToLoadImage() {}
            }
        )
    }
    fun setData(images: List<FetchLocationResponseClasses.Image>){
        this.images = images.toMutableList()
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = images.size
    class ViewHolder(val binding: ItemImagesBinding) : RecyclerView.ViewHolder(binding.root)
}

