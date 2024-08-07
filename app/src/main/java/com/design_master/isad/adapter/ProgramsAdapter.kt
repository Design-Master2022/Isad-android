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
import com.design_master.isad.model.listeners.ScientificProgramListener
import com.design_master.isad.model.network.response.EnableNotificationResponseClasses
import com.design_master.isad.model.network.response.GetAllScientificProgramsResponseClasses
import com.design_master.isad.utils.helper.Helper

class ProgramsAdapter(private val listener: ScientificProgramListener): RecyclerView.Adapter<ProgramsAdapter.ViewHolder>() {
    private var programs = mutableListOf<GetAllScientificProgramsResponseClasses.ScientificProgram>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemProgramOrWorkshopBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.title.text = programs[position].speaker.name
        holder.binding.description.text = programs[position].title

        holder.binding.startDate.text = programs[position].timeFrom.split(" ").first()
        holder.binding.endDate.text = programs[position].timeTo.split(" ").first()
        holder.binding.btnNotification.setImageResource(if (programs[position].notification) R.drawable.ic_notification else R.drawable.ic_enable_notifications)
        holder.binding.btnWishlist.setImageResource(if (programs[position].wishlist) R.drawable.ic_wishlist else R.drawable.ic_add_to_wishlist)
        holder.binding.btnNotes.visibility = if (programs[position].speaker.isSpeaker == Constants.SPEAKER) View.VISIBLE else View.GONE
        holder.binding.btnNotification.visibility = if (programs[position].speaker.isSpeaker == Constants.SPEAKER) View.VISIBLE else View.GONE
        holder.binding.btnWishlist.visibility = if (programs[position].speaker.isSpeaker == Constants.SPEAKER) View.VISIBLE else View.GONE

        holder.binding.img.invisible()
        holder.binding.shimmerImg.show()
        Helper.loadImage(
            url = "speakers/${programs[position].speaker.image}",
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

        holder.binding.layoutImg2.visibility = if (programs[position].vsSpeaker == null) View.GONE else View.VISIBLE

        if (programs[position].vsSpeaker != null){

            holder.binding.title.text = "${programs[position].speaker.name} vs ${programs[position].vsSpeaker!!.name}"

            holder.binding.img2.invisible()
            holder.binding.shimmerImg2.show()
            Helper.loadImage(
                url = "speakers/${programs[position].vsSpeaker!!.image}",
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
            listener.onClick(programs[position])
        }
        holder.binding.btnNotification.setOnClickListener {
            if (programs[position].notification){
                holder.binding.btnNotification.hide()
                holder.binding.notificationProgress.show()

                listener.onDisableNotification(programs[position], object: DisableNotificationListener{
                    override fun onNotificationDisabled() {
                        holder.binding.btnNotification.show()
                        holder.binding.notificationProgress.hide()

                        programs[position].notification = false
                        holder.binding.btnNotification.setImageResource(R.drawable.ic_enable_notifications)
                    }
                    override fun onFailedToDisableNotification() {
                        holder.binding.btnNotification.show()
                        holder.binding.notificationProgress.hide()

                        programs[position].notification = true
                        holder.binding.btnNotification.setImageResource(R.drawable.ic_notification)
                    }
                })
            }
            else {
                holder.binding.btnNotification.hide()
                holder.binding.notificationProgress.show()

                listener.onEnableNotification(programs[position], object: EnableNotificationListener{
                    override fun onNotificationEnabled(data: EnableNotificationResponseClasses.Data) {
                        holder.binding.btnNotification.show()
                        holder.binding.notificationProgress.hide()

                        programs[position].notification = true
                        holder.binding.btnNotification.setImageResource(R.drawable.ic_notification)
                    }
                    override fun onFailedToEnableNotification() {
                        holder.binding.btnNotification.show()
                        holder.binding.notificationProgress.hide()

                        programs[position].notification = false
                        holder.binding.btnNotification.setImageResource(R.drawable.ic_enable_notifications)
                    }
                })
            }
        }
        holder.binding.btnWishlist.setOnClickListener {
            if (programs[position].wishlist){
                holder.binding.btnWishlist.hide()
                holder.binding.wishlistProgress.show()

                listener.onRemoveFromWishlist(programs[position], object: RemoveFromWishlistListener{
                    override fun onRemovedFromWishlist() {
                        holder.binding.btnWishlist.show()
                        holder.binding.wishlistProgress.hide()

                        programs[position].wishlist = false
                        holder.binding.btnWishlist.setImageResource(R.drawable.ic_add_to_wishlist)
                    }
                    override fun onFailedToRemove() {
                        holder.binding.btnWishlist.show()
                        holder.binding.wishlistProgress.hide()

                        programs[position].wishlist = true
                        holder.binding.btnWishlist.setImageResource(R.drawable.ic_wishlist)
                    }
                })
            }
            else {
                holder.binding.btnWishlist.hide()
                holder.binding.wishlistProgress.show()

                listener.onAddToWishlist(programs[position], object: AddToWishlistListener{
                    override fun onAddedToWishlist() {
                        holder.binding.btnWishlist.show()
                        holder.binding.wishlistProgress.hide()

                        programs[position].wishlist = true
                        holder.binding.btnWishlist.setImageResource(R.drawable.ic_wishlist)
                    }
                    override fun onFailedToAdd() {
                        holder.binding.btnWishlist.show()
                        holder.binding.wishlistProgress.hide()

                        programs[position].wishlist = false
                        holder.binding.btnWishlist.setImageResource(R.drawable.ic_add_to_wishlist)
                    }
                })
            }
        }
    }

    override fun getItemCount(): Int = programs.size

    fun setData(programs: List<GetAllScientificProgramsResponseClasses.ScientificProgram>){
        this.programs = programs.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemProgramOrWorkshopBinding) : RecyclerView.ViewHolder(binding.root)
}

