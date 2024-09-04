package com.design_master1.isad.model.listeners

import com.design_master1.isad.model.network.response.EnableNotificationResponseClasses

interface EnableNotificationListener {
    fun onNotificationEnabled(data: EnableNotificationResponseClasses.Data)
    fun onFailedToEnableNotification()
}