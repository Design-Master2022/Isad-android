package com.design_master.isad.model.listeners

import com.design_master.isad.model.network.response.FetchMenuResponseClasses


interface MenuListener {
    fun onClick(menuItem: FetchMenuResponseClasses.MenuItem)
}