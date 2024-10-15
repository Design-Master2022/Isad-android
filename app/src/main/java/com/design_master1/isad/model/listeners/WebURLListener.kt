package com.design_master1.isad.model.listeners

import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebView

interface WebURLListener {
    fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean
    fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?)
    fun onPageFinished(view: WebView?, url: String?)
}