package com.dena.autum_hackathon_b.cassette.data.cassette.filesystem

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject

class MediaCache @Inject constructor(@ApplicationContext private val context: Context) {
    fun createNewMediaVideoFile(): File {
        val id = UUID.randomUUID()
        return File(getMediaCacheDirectory(), "$id")
    }

    private fun getMediaCacheDirectory(): File {
        val result = File(context.cacheDir, "media_cache")
        if (result.exists().not()) {
            result.mkdirs()
        }
        return result
    }

    fun copyContentToFileOutputStream(uri: Uri, outputStream: FileOutputStream) {
        context.contentResolver
            .openInputStream(uri)
            ?.use {
                it.copyTo(outputStream)
            }
    }
}