package com.design_master.isad.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.design_master.isad.R
import com.design_master.isad.databinding.ItemProgramOrWorkshopBinding
import com.design_master.isad.extensions.hide
import com.design_master.isad.extensions.invisible
import com.design_master.isad.extensions.show
import com.design_master.isad.model.constants.Constants
import com.design_master.isad.model.listeners.AddToWishlistListener
import com.design_master.isad.model.listeners.DisableNotificationListener
import com.design_master.isad.model.listeners.EnableNotificationListener
import com.design_master.isad.model.listeners.RemoveFromWishlistListener
import com.design_master.isad.model.listeners.WorkShopListener
import com.design_master.isad.model.network.response.EnableNotificationResponseClasses
import com.design_master.isad.model.network.response.GetAllWorkShopsResponseClasses
import com.design_master.isad.utils.helper.Helper

class WorkShopsAdapter(private val listener: WorkShopListener): RecyclerView.Adapter<WorkShopsAdapter.ViewHolder>() {
    private var workshops = mutableListOf<GetAllWorkShopsResponseClasses.WorkShop>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemProgramOrWorkshopBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.title.text = workshops[position].speaker.name
        holder.binding.description.text = workshops[position].title

        holder.binding.startDate.text = workshops[position].timeFrom.split(" ").first()
        holder.binding.endDate.text = workshops[position].timeTo.split(" ").first()
        holder.binding.btnNotification.setImageResource(if (workshops[position].notification) R.drawable.ic_notification else R.drawable.ic_enable_notifications)
        holder.binding.btnWishlist.setImageResource(if (workshops[position].wishlist) R.drawable.ic_wishlist else R.drawable.ic_add_to_wishlist)
        holder.binding.btnNotes.visibility = if (workshops[position].speaker.isSpeaker == Constants.SPEAKER) View.VISIBLE else View.GONE
        holder.binding.btnNotification.visibility = if (workshops[position].speaker.isSpeaker == Constants.SPEAKER) View.VISIBLE else View.GONE
        holder.binding.btnWishlist.visibility = if (workshops[position].speaker.isSpeaker == Constants.SPEAKER) View.VISIBLE else View.GONE

        holder.binding.img.invisible()
        holder.binding.shimmerImg.show()
        Helper.loadImage(
            url = "speakers/${workshops[position].speaker.image}",
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

        holder.binding.layoutImg2.visibility = if (workshops[position].vsSpeaker == null) View.GONE else View.VISIBLE

        if (workshops[position].vsSpeaker != null){

            holder.binding.title.text = "${workshops[position].speaker.name} vs ${workshops[position].vsSpeaker!!.name}"

            holder.binding.img2.invisible()
            holder.binding.shimmerImg2.show()
            Helper.loadImage(
                url = "speakers/${workshops[position].vsSpeaker!!.image}",
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
            listener.onClick(workshops[position])
        }
        holder.binding.btnNotification.setOnClickListener {
            if (workshops[position].notification){
                holder.binding.btnNotification.hide()
                holder.binding.notificationProgress.show()

                listener.onDisableNotification(workshops[position], object: DisableNotificationListener {
                    override fun onNotificationDisabled() {
                        holder.binding.btnNotification.show()
                        holder.binding.notificationProgress.hide()

                        workshops[position].notification = false
                        holder.binding.btnNotification.setImageResource(R.drawable.ic_enable_notifications)
                    }
                    override fun onFailedToDisableNotification() {
                        holder.binding.btnNotification.show()
                        holder.binding.notificationProgress.hide()

                        workshops[position].notification = true
                        holder.binding.btnNotification.setImageResource(R.drawable.ic_notification)
                    }
                })
            }
            else {
                holder.binding.btnNotification.hide()
                holder.binding.notificationProgress.show()

                listener.onEnableNotification(workshops[position], object:
                    EnableNotificationListener {
                    override fun onNotificationEnabled(data: EnableNotificationResponseClasses.Data) {
                        holder.binding.btnNotification.show()
                        holder.binding.notificationProgress.hide()

                        workshops[position].notification = true
                        holder.binding.btnNotification.setImageResource(R.drawable.ic_notification)
                    }
                    override fun onFailedToEnableNotification() {
                        holder.binding.btnNotification.show()
                        holder.binding.notificationProgress.hide()

                        workshops[position].notification = false
                        holder.binding.btnNotification.setImageResource(R.drawable.ic_enable_notifications)
                    }
                })
            }
        }
        holder.binding.btnWishlist.setOnClickListener {
            if (workshops[position].wishlist){
                holder.binding.btnWishlist.hide()
                holder.binding.wishlistProgress.show()

                listener.onRemoveFromWishlist(workshops[position], object: RemoveFromWishlistListener {
                    override fun onRemovedFromWishlist() {
                        holder.binding.btnWishlist.show()
                        holder.binding.wishlistProgress.hide()

                        workshops[position].wishlist = false
                        holder.binding.btnWishlist.setImageResource(R.drawable.ic_add_to_wishlist)
                    }
                    override fun onFailedToRemove() {
                        holder.binding.btnWishlist.show()
                        holder.binding.wishlistProgress.hide()

                        workshops[position].wishlist = true
                        holder.binding.btnWishlist.setImageResource(R.drawable.ic_wishlist)
                    }
                })
            }
            else {
                holder.binding.btnWishlist.hide()
                holder.binding.wishlistProgress.show()

                listener.onAddToWishlist(workshops[position], object: AddToWishlistListener {
                    override fun onAddedToWishlist() {
                        holder.binding.btnWishlist.show()
                        holder.binding.wishlistProgress.hide()

                        workshops[position].wishlist = true
                        holder.binding.btnWishlist.setImageResource(R.drawable.ic_wishlist)
                    }
                    override fun onFailedToAdd() {
                        holder.binding.btnWishlist.show()
                        holder.binding.wishlistProgress.hide()

                        workshops[position].wishlist = false
                        holder.binding.btnWishlist.setImageResource(R.drawable.ic_add_to_wishlist)
                    }
                })
            }
        }
    }
    override fun getItemCount(): Int = workshops.size
    fun setData(workshops: List<GetAllWorkShopsResponseClasses.WorkShop>){
        this.workshops = workshops.toMutableList()
        notifyDataSetChanged()
    }
    class ViewHolder(val binding: ItemProgramOrWorkshopBinding) : RecyclerView.ViewHolder(binding.root)
}

