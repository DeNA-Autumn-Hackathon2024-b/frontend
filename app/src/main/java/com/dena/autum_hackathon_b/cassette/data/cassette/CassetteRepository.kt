package com.dena.autum_hackathon_b.cassette.data.cassette

import android.net.Uri
import com.dena.autum_hackathon_b.cassette.entity.CachedAudioFile
import com.dena.autum_hackathon_b.cassette.entity.Cassette
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.io.File

interface CassetteRepository {
    suspend fun getCassette(id: String): Cassette
    suspend fun uploadAudio(
        audioFile: File,
        mimeType: String,
        name: String,
        durationSec: Int,
        number: Int
    ): String

    suspend fun cacheAudio(audioUri: Uri): CachedAudioFile
}

@Module
@InstallIn(SingletonComponent::class)
abstract class CassetteRepositoryModule {
    @Binds
    abstract fun bindCassetteRepository(impl: CassetteRepositoryImpl): CassetteRepository
}
