package com.dena.autum_hackathon_b.cassette.data.cassette.reset_api

import android.content.Context
import android.net.Uri
import com.dena.autum_hackathon_b.cassette.entity.Cassette
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import retrofit2.Retrofit
import retrofit2.http.GET
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class CassetteRestApi @Inject constructor(
    retrofit: Retrofit,
    @ApplicationContext private val context: Context
) {
    interface CassetteService {
        @GET("/v1/cassette")
        suspend fun getCassette(): GetCassetteResponse
    }

    data class GetCassetteResponse(
        val id: String
    )

    private val service = retrofit.create(CassetteService::class.java)

    suspend fun getCassette(): Cassette {
        val response = service.getCassette()

        return Cassette(id = response.id)
    }

    suspend fun uploadAudioFile(audioFile: File, mimeType: String) {
        val requestBody = createRequestBodyAsInputStream(audioFile, mimeType)
//        awsService.postVideoFile(s3ObjectUrl, requestBody)
    }

    private fun createRequestBodyAsInputStream(audioFile: File, mimeType: String): RequestBody {

        return object : RequestBody() {
            override fun contentType(): MediaType? = mimeType.toMediaTypeOrNull()

            override fun writeTo(sink: BufferedSink) {
                audioFile.inputStream().use {
                    it.source().use(sink::writeAll)
                }
            }

            override fun contentLength(): Long = audioFile.length()
        }
    }
}