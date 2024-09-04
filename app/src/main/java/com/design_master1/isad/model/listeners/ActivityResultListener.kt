package com.design_master1.isad.model.listeners

import android.content.Intent

interface ActivityResultListener {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}
