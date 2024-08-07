package com.design_master.isad.model.listeners

import com.design_master.isad.model.network.response.GetHomeDataResponseClasses

interface SponsorListener {
    fun onClick(sponsor: GetHomeDataResponseClasses.Sponsor)
}