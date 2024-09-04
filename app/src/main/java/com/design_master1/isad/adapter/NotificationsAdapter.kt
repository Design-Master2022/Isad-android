package com.design_master1.isad.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.design_master1.isad.databinding.ItemNotificationBinding
import com.design_master1.isad.model.network.response.GetEnabledNotificationsResponseClasses

class NotificationsAdapter: RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {
    private var notifications = mutableListOf<GetEnabledNotificationsResponseClasses.Notification>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemNotificationBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.title.text = notifications[position].title
        holder.binding.message.text = notifications[position].message
    }
    override fun getItemCount(): Int = notifications.size
    fun setData(notifications: List<GetEnabledNotificationsResponseClasses.Notification>){
        this.notifications = notifications.toMutableList()
        notifyDataSetChanged()
    }
    class ViewHolder(val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root)
}

