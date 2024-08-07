package com.design_master.isad.adapter

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.design_master.isad.databinding.ItemSpeakerBinding
import com.design_master.isad.extensions.hide
import com.design_master.isad.extensions.invisible
import com.design_master.isad.extensions.show
import com.design_master.isad.model.network.response.FetchAllSpeakersResponseClasses
import com.design_master.isad.utils.helper.Helper

class SpeakersAdapter: RecyclerView.Adapter<SpeakersAdapter.ViewHolder>() {
    private var speakers = mutableListOf<FetchAllSpeakersResponseClasses.Speaker>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemSpeakerBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.name.text = speakers[position].name
        holder.binding.description.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(speakers[position].detail, Html.FROM_HTML_MODE_COMPACT)
        else Html.fromHtml(speakers[position].detail)


        holder.binding.img.invisible()
        holder.binding.shimmerImg.show()
        Helper.loadImage(
            url = "speakers/${speakers[position].image}",
            imageView = holder.binding.img,
            isCompleteURL = false,
            listener = object: Helper.LoadImageListener{
                override fun onImageLoaded() {
                    holder.binding.img.show()
                    holder.binding.shimmerImg.hide()
                }

                override fun onFailedToLoadImage() {
                    holder.binding.img.show()
                    holder.binding.shimmerImg.hide()
                }
            }
        )
        if (speakers.isNotEmpty()){
            holder.binding.imgFlag.invisible()
            holder.binding.shimmerImgFlag.show()
            Helper.loadImage(
                url = "flags/${speakers[position].country.first().flagCode}.png",
                imageView = holder.binding.imgFlag,
                isCompleteURL = false,
                listener = object: Helper.LoadImageListener{
                    override fun onImageLoaded() {
                        holder.binding.imgFlag.show()
                        holder.binding.shimmerImgFlag.hide()
                    }

                    override fun onFailedToLoadImage() {
                        holder.binding.imgFlag.show()
                        holder.binding.shimmerImgFlag.hide()
                    }
                }
            )
        }

        holder.binding.parent.setOnClickListener {
            holder.binding.description.visibility = if (holder.binding.description.isVisible) View.GONE else View.VISIBLE
        }
    }

    override fun getItemCount(): Int = speakers.size

    fun setData(speakers: List<FetchAllSpeakersResponseClasses.Speaker>){
        this.speakers = speakers.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemSpeakerBinding) : RecyclerView.ViewHolder(binding.root)
}

