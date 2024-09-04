package com.design_master1.isad.model.listeners

import com.design_master1.isad.model.network.response.GetWishlistResponseClasses

interface WishlistRequestListener {
    fun onClick(wishlist: GetWishlistResponseClasses.WishlistItem)
    fun onEnableNotification(wishlist: GetWishlistResponseClasses.WishlistItem, listener: EnableNotificationListener)
    fun onDisableNotification(wishlist: GetWishlistResponseClasses.WishlistItem, listener: DisableNotificationListener)
    fun onRemoveFromWishlist(wishlist: GetWishlistResponseClasses.WishlistItem, listener: RemoveFromWishlistListener)
}