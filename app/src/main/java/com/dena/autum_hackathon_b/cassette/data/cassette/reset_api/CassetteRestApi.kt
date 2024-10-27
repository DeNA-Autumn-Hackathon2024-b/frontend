package com.dena.autum_hackathon_b.cassette.data.cassette.reset_api

import android.content.Context
import com.dena.autum_hackathon_b.cassette.entity.Cassette
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.Serializable
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.BufferedSink
import okio.source
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File
import javax.inject.Inject


class CassetteRestApi @Inject constructor(
    retrofit: Retrofit,
    @ApplicationContext private val context: Context
) {
    interface CassetteService {
        @GET("/v1/cassette")
        suspend fun getCassette(): GetCassetteResponse

        @Multipart
        @POST("/song")
        suspend fun uploadAudioFile(
            @Part cassetteId: MultipartBody.Part,
            @Part userId: MultipartBody.Part,
            @Part songNumber: MultipartBody.Part,
            @Part songTime: MultipartBody.Part,
            @Part name: MultipartBody.Part,
            @Part song: MultipartBody.Part
        ): UploadAudioFileResponse
    }

    data class GetCassetteResponse(
        val id: String
    )

    @Serializable
    data class UploadAudioFileResponse(
        val url: String
    )

    private val service = retrofit.create(CassetteService::class.java)

    suspend fun getCassette(): Cassette {
        val response = service.getCassette()

        return Cassette(id = response.id)
    }

    suspend fun uploadAudioFile(
        audioFile: File,
        mimeType: String,
        name: String,
        number: Int,
        durationSec: Int,
    ): String {
        val requestBody = createRequestBodyAsInputStream(audioFile, mimeType)

        val cassetteId = createFormData("cassette_id", "cassette1")
        val userId = createFormData("user_id", "user1")
        val songNumber = createFormData("song_number", "$number")
        val songTime = createFormData("song_time", "$durationSec")
        val name = createFormData("name", name)

        val song: MultipartBody.Part = createFormData(
            "song", "song",
            audioFile.asRequestBody(mimeType.toMediaType())
        )

        val response = service.uploadAudioFile(
            cassetteId = cassetteId,
            userId = userId,
            songNumber = songNumber,
            songTime = songTime,
            name = name,
            song = song
        )

        return response.url
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