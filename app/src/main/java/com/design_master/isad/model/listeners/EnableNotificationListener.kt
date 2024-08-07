package com.design_master.isad.model.listeners

import com.design_master.isad.model.network.response.EnableNotificationResponseClasses

interface EnableNotificationListener {
    fun onNotificationEnabled(data: EnableNotificationResponseClasses.Data)
    fun onFailedToEnableNotification()
}