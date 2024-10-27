package com.dena.autum_hackathon_b.cassette.data.cassette

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.dena.autum_hackathon_b.cassette.data.cassette.filesystem.MediaCache
import com.dena.autum_hackathon_b.cassette.data.cassette.reset_api.CassetteRestApi
import com.dena.autum_hackathon_b.cassette.entity.CachedAudioFile
import com.dena.autum_hackathon_b.cassette.entity.Cassette
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CassetteRepositoryImpl @Inject constructor(
    private val cassetteRestApi: CassetteRestApi,
    private val mediaCache: MediaCache,
    @ApplicationContext private val context: Context
) : CassetteRepository {
    override suspend fun getCassette(id: String): Cassette {
        return Cassette(id = id)
    }

    override suspend fun uploadAudio(audioFile: File, mimeType: String) {
        cassetteRestApi.uploadAudioFile(audioFile, mimeType)
    }

    override suspend fun cacheAudio(audioUri: Uri): CachedAudioFile {
        val cacheFile = mediaCache.createNewMediaVideoFile()

        cacheFile.outputStream().use {
            mediaCache.copyContentToFileOutputStream(audioUri, it)
        }

        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(audioUri) ?: "audio/mp3"

        val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)
        val cursor = contentResolver.query(audioUri, projection, null, null, null)
        var fileName = ""
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                fileName = cursor.getString(
                    cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                )
            }
            cursor.close()
        }

        return CachedAudioFile(
            audioFile =  cacheFile,
            mimeType = mimeType,
            fileName = fileName
        )
    }
}