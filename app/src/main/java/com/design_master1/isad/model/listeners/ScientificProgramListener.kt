package com.design_master1.isad.model.listeners

import com.design_master1.isad.model.network.response.GetAllScientificProgramsResponseClasses

interface ScientificProgramListener {
    fun onClick(program: GetAllScientificProgramsResponseClasses.ScientificProgram)
    fun onEnableNotification(program: GetAllScientificProgramsResponseClasses.ScientificProgram, listener: EnableNotificationListener)
    fun onDisableNotification(program: GetAllScientificProgramsResponseClasses.ScientificProgram, listener: DisableNotificationListener)
    fun onAddToWishlist(program: GetAllScientificProgramsResponseClasses.ScientificProgram, listener: AddToWishlistListener)
    fun onRemoveFromWishlist(program: GetAllScientificProgramsResponseClasses.ScientificProgram, listener: RemoveFromWishlistListener)
}