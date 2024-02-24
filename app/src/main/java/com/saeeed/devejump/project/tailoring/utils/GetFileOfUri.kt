package com.saeeed.devejump.project.tailoring.utils

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class GetFileOfUri(
    val context: Context
)

{
    fun getFileFromUri(
        uri: Uri
    ): MultipartBody.Part {
        val fileDire = context.filesDir
        val file = File(fileDire, "image.png")
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        inputStream!!.copyTo(outputStream)
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData("AvatarFile", file.name, requestBody)
    }
}