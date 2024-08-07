package com.design_master.isad.model.constants

import android.Manifest
import com.google.android.gms.maps.model.LatLng

object Constants {

    const val IMAGES_URL_EXTENSION = "uploads/"
    const val API_VERSION = "v1/"

    const val CHAIRMAN_MESSAGE_URL = "https://kifmc.com/#sections3"
    const val COMMITTEE_URL = "https://kifmc.com/#sections4"
    const val SPONSORS_URL = "https://kifmc.com/index.html#sections7"
    const val HOTELS_AND_VISA = "https://kifmc.com/index.html#sections8"
    const val POSTERS_URL = "https://kifmc.com/poster.php"
    const val ABOUT_KUWAIT_URL = "https://kifmc.com/about-kuwait.html"


    const val CONFERENCE_READ_MORE_URL = "https://kifmc.com/index.html#sections2"

    const val INSTAGRAM_URL = "https://www.instagram.com/swaacelso2024/"
    const val LINKED_IN_URL = "https://www.linkedin.com/in/swaac-elso-elso-2b079528a/"
    const val FACEBOOK_URL = "https://www.facebook.com/swaacelso2024"
    const val TWITTER_URL = "https://twitter.com/swaacelso2024"
    const val WHATSAPP_URL = "https://api.whatsapp.com/send?phone=96565084326"


    const val SPEAKER = "1"
    const val SCIENTIFIC_PROGRAMS = "scientific_programs"
    const val WORKSHOPS = "workshops"

    val DEFAULT_LATLNG = LatLng(29.38199544925234, 47.97034010813074)

    /** Permissions */
    /**  ALSO UPDATE IN HELPER FOR PERMISSIONS  */
    const val COURSE_LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
    const val FINE_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
    const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    const val READ_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
    const val WRITE_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE
    const val RECORD_AUDIO_PERMISSION = Manifest.permission.RECORD_AUDIO
    const val POST_NOTIFICATION_PERMISSION = Manifest.permission.POST_NOTIFICATIONS
}