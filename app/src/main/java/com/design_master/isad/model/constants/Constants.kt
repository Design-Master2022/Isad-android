package com.design_master.isad.model.constants

import android.Manifest
import com.google.android.gms.maps.model.LatLng

object Constants {

    const val IMAGES_URL_EXTENSION = "uploads/"
    const val API_VERSION = "v1/"

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