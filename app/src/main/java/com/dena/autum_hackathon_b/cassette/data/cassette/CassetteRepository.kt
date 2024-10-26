package com.dena.autum_hackathon_b.cassette.data.cassette

import com.dena.autum_hackathon_b.cassette.entity.Cassette
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

interface CassetteRepository {
    suspend fun getCassette(id: String): Cassette
}

@Module
@InstallIn(SingletonComponent::class)
abstract class CassetteRepositoryModule {
    @Binds
    abstract fun bindCassetteRepository(impl: CassetteRepositoryImpl): CassetteRepository
}
