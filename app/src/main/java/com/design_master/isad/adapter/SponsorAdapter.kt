package com.design_master.isad.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.design_master.isad.databinding.ItemBrandBinding
import com.design_master.isad.extensions.invisible
import com.design_master.isad.extensions.show
import com.design_master.isad.model.listeners.SponsorListener
import com.design_master.isad.model.network.response.GetHomeDataResponseClasses
import com.design_master.isad.utils.helper.Helper

class SponsorAdapter(val listener: SponsorListener): RecyclerView.Adapter<SponsorAdapter.ViewHolder>() {
    var sponsors = mutableListOf<GetHomeDataResponseClasses.Sponsor>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemBrandBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.normalImg.invisible()
        holder.binding.shimmerImg.show()
        Helper.loadImage(
            url = "brands/${sponsors[position].image}",
            isCompleteURL = false,
            imageView = holder.binding.normalImg,
            listener = object: Helper.LoadImageListener{
                override fun onImageLoaded() {
                    holder.binding.normalImg.show()
                    holder.binding.shimmerImg.invisible()
                }

                override fun onFailedToLoadImage() {
                    holder.binding.normalImg.show()
                    holder.binding.shimmerImg.invisible()
                }
            }
        )
        holder.binding.parent.setOnClickListener {
            listener.onClick(sponsors[position])
        }
    }
    fun setData(brands: List<GetHomeDataResponseClasses.Sponsor>){
        this.sponsors = brands.toMutableList()
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = sponsors.size
    class ViewHolder(val binding: ItemBrandBinding) : RecyclerView.ViewHolder(binding.root)
}

