package com.design_master1.isad.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.design_master1.isad.R
import com.design_master1.isad.databinding.ItemTracksBinding
import com.design_master1.isad.model.listeners.TrackListener
import com.design_master1.isad.model.network.response.GetAllScientificProgramsResponseClasses

class TracksAdapter(private val context: Context, private val listener: TrackListener): RecyclerView.Adapter<TracksAdapter.ViewHolder>() {

    private var tracks = mutableListOf<GetAllScientificProgramsResponseClasses.Track>()
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTracksBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.title1.text = tracks[position].title1
        holder.binding.title2.text = tracks[position].title2
        holder.binding.title1.setTextColor(ContextCompat.getColor(context, if (position == selectedPosition) R.color.white else R.color.primary))
        holder.binding.title2.setTextColor(ContextCompat.getColor(context, if (position == selectedPosition) R.color.white else R.color.primary))

        holder.binding.card.setCardBackgroundColor(ContextCompat.getColor(context, if (position == selectedPosition) R.color.primary else R.color.white))

        holder.binding.card.setOnClickListener {
            selectedPosition = position
            listener.onClick(tracks[position].track)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = tracks.size

    fun setData(tracks: List<GetAllScientificProgramsResponseClasses.Track>){
        this.tracks = tracks.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemTracksBinding) : RecyclerView.ViewHolder(binding.root)
}

