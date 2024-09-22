package com.design_master1.isad.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.design_master1.isad.R
import com.design_master1.isad.databinding.ItemDaysBinding
import com.design_master1.isad.model.listeners.DayListener

class DaysAdapter(private val context: Context, private val listener: DayListener): RecyclerView.Adapter<DaysAdapter.ViewHolder>() {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDaysBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.day.text = "Day ${position.plus(1)}"
        holder.binding.card.setCardBackgroundColor(ContextCompat.getColor(context, if (position == selectedPosition) R.color.primary else R.color.white))
        holder.binding.day.setTextColor(ContextCompat.getColor(context, if (position == selectedPosition) R.color.white else R.color.primary))

        holder.binding.card.setOnClickListener {
            selectedPosition = position
            listener.onClick(selectedPosition)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = 3


    class ViewHolder(val binding: ItemDaysBinding) : RecyclerView.ViewHolder(binding.root)
}

