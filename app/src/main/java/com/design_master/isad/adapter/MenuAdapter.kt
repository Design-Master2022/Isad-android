package com.design_master.isad.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.design_master.isad.databinding.DrawerItemBinding
import com.design_master.isad.model.listeners.MenuListener
import com.design_master.isad.model.network.response.FetchMenuResponseClasses
import com.design_master.isad.utils.helper.Helper

class MenuAdapter(
    private val listener: MenuListener
): RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    private var menu = mutableListOf<FetchMenuResponseClasses.MenuItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DrawerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.title.text = menu[position].name

        Helper.loadImage(
            url = "menus/${menu[position].image}",
            imageView = holder.binding.img,
            isCompleteURL = false,
            listener = object: Helper.LoadImageListener{
                override fun onImageLoaded() {}
                override fun onFailedToLoadImage() {}
            }
        )

        holder.binding.parent.setOnClickListener {
            listener.onClick(menu[position])
        }
    }

    override fun getItemCount(): Int = menu.size

    fun setData(menu: List<FetchMenuResponseClasses.MenuItem>){
        this.menu = menu.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: DrawerItemBinding) : RecyclerView.ViewHolder(binding.root)
}

