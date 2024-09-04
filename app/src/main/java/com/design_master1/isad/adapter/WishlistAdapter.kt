package com.design_master1.isad.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.design_master1.isad.R
import com.design_master1.isad.databinding.ItemProgramOrWorkshopBinding
import com.design_master1.isad.extensions.hide
import com.design_master1.isad.extensions.invisible
import com.design_master1.isad.extensions.show
import com.design_master1.isad.model.constants.Constants
import com.design_master1.isad.model.listeners.DisableNotificationListener
import com.design_master1.isad.model.listeners.EnableNotificationListener
import com.design_master1.isad.model.listeners.RemoveFromWishlistListener
import com.design_master1.isad.model.listeners.WishlistRequestListener
import com.design_master1.isad.model.network.response.EnableNotificationResponseClasses
import com.design_master1.isad.model.network.response.GetWishlistResponseClasses
import com.design_master1.isad.utils.helper.Helper

class WishlistAdapter(private val listener: WishlistRequestListener): RecyclerView.Adapter<WishlistAdapter.ViewHolder>() {
    var wishlist = mutableListOf<GetWishlistResponseClasses.WishlistItem>()
    var selectedWishlistItem: GetWishlistResponseClasses.WishlistItem? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemProgramOrWorkshopBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.title.text = wishlist[position].dayWise.speaker.name
        holder.binding.description.text = wishlist[position].dayWise.title

        holder.binding.startDate.text = wishlist[position].dayWise.timeFrom.split(" ").first()
        holder.binding.endDate.text = wishlist[position].dayWise.timeTo.split(" ").first()
        holder.binding.btnWishlist.setImageResource(R.drawable.ic_wishlist)
        holder.binding.btnNotification.setImageResource(if (wishlist[position].dayWise.notification) R.drawable.ic_notification else R.drawable.ic_enable_notifications)
        holder.binding.btnNotes.visibility = if (wishlist[position].dayWise.speaker.isSpeaker == Constants.SPEAKER) View.VISIBLE else View.GONE

        holder.binding.img.invisible()
        holder.binding.shimmerImg.show()
        Helper.loadImage(
            url = "speakers/${wishlist[position].dayWise.speaker.image}",
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

        holder.binding.layoutImg2.visibility = if (wishlist[position].dayWise.vsSpeaker == null) View.GONE else View.VISIBLE

        if (wishlist[position].dayWise.vsSpeaker != null){

            holder.binding.title.text = "${wishlist[position].dayWise.speaker.name} vs ${wishlist[position].dayWise.vsSpeaker!!.name}"

            holder.binding.img2.invisible()
            holder.binding.shimmerImg2.show()
            Helper.loadImage(
                url = "speakers/${wishlist[position].dayWise.vsSpeaker!!.image}",
                imageView = holder.binding.img2,
                isCompleteURL = false,
                listener = object: Helper.LoadImageListener{
                    override fun onImageLoaded() {
                        holder.binding.img2.show()
                        holder.binding.shimmerImg2.hide()
                    }

                    override fun onFailedToLoadImage() {
                        holder.binding.img2.show()
                        holder.binding.shimmerImg2.hide()
                    }
                }
            )
        }

        holder.binding.parent.setOnClickListener {
            listener.onClick(wishlist[position])
        }
        holder.binding.btnNotification.setOnClickListener {
            if (wishlist[position].dayWise.notification){
                holder.binding.btnNotification.hide()
                holder.binding.notificationProgress.show()

                listener.onDisableNotification(wishlist[position], object: DisableNotificationListener {
                    override fun onNotificationDisabled() {
                        holder.binding.btnNotification.show()
                        holder.binding.notificationProgress.hide()

                        wishlist[position].dayWise.notification = false
                        holder.binding.btnNotification.setImageResource(R.drawable.ic_enable_notifications)
                    }
                    override fun onFailedToDisableNotification() {
                        holder.binding.btnNotification.show()
                        holder.binding.notificationProgress.hide()

                        wishlist[position].dayWise.notification = true
                        holder.binding.btnNotification.setImageResource(R.drawable.ic_notification)
                    }
                })
            }
            else {
                holder.binding.btnNotification.hide()
                holder.binding.notificationProgress.show()

                listener.onEnableNotification(wishlist[position], object: EnableNotificationListener {
                    override fun onNotificationEnabled(data: EnableNotificationResponseClasses.Data) {
                        holder.binding.btnNotification.show()
                        holder.binding.notificationProgress.hide()

                        wishlist[position].dayWise.notification = true
                        holder.binding.btnNotification.setImageResource(R.drawable.ic_notification)
                    }
                    override fun onFailedToEnableNotification() {
                        holder.binding.btnNotification.show()
                        holder.binding.notificationProgress.hide()

                        wishlist[position].dayWise.notification = false
                        holder.binding.btnNotification.setImageResource(R.drawable.ic_enable_notifications)
                    }
                })
            }
        }
        holder.binding.btnWishlist.setOnClickListener {
            selectedWishlistItem = wishlist[position]
            listener.onRemoveFromWishlist(wishlist[position], object: RemoveFromWishlistListener {
                override fun onRemovedFromWishlist() {
                }
                override fun onFailedToRemove() {
                }
            })
        }
    }
    override fun getItemCount(): Int = wishlist.size
    fun setData(wishlist: List<GetWishlistResponseClasses.WishlistItem>){
        this.wishlist = wishlist.toMutableList()
        notifyDataSetChanged()
    }
    class ViewHolder(val binding: ItemProgramOrWorkshopBinding) : RecyclerView.ViewHolder(binding.root)
}

