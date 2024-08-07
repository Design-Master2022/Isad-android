package com.design_master.isad.model.listeners

import com.design_master.isad.model.network.response.GetAllWorkShopsResponseClasses

interface WorkShopListener {
    fun onClick(workShop: GetAllWorkShopsResponseClasses.WorkShop)
    fun onEnableNotification(workShop: GetAllWorkShopsResponseClasses.WorkShop, listener: EnableNotificationListener)
    fun onDisableNotification(workShop: GetAllWorkShopsResponseClasses.WorkShop, listener: DisableNotificationListener)
    fun onAddToWishlist(workShop: GetAllWorkShopsResponseClasses.WorkShop, listener: AddToWishlistListener)
    fun onRemoveFromWishlist(workShop: GetAllWorkShopsResponseClasses.WorkShop, listener: RemoveFromWishlistListener)
}