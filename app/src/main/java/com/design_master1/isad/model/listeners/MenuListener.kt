package com.design_master1.isad.model.listeners

import com.design_master1.isad.model.network.response.FetchMenuResponseClasses


interface MenuListener {
    fun onClick(menuItem: FetchMenuResponseClasses.MenuItem)
}