package com.design_master.isad.utils.multipart_helper

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class MultipartHelper {
    fun createRequestBody(payload: String): RequestBody {
        return RequestBody.create("multipart/form-data".toMediaType(),payload )
    }

    /**
     * @param partKey: server identifier for its file.
     * @param imageName: original name of image.
     * @param image: file pointing to real image
     */
    fun createMultipartBodyPartImage(partKey: String, imageName: String, image: File): MultipartBody.Part {
        val requestBody: RequestBody = RequestBody.create("image/*".toMediaType(),image)
        return MultipartBody.Part.createFormData(partKey, imageName, requestBody)
    }

    fun createMultipartBodyPartVideo(partKey: String, imageName: String, video: File): MultipartBody.Part {
        val requestBody: RequestBody = RequestBody.create("video/*".toMediaType(),video)
        return MultipartBody.Part.createFormData(partKey, imageName, requestBody)
    }
}