package com.dena.autum_hackathon_b.cassette.data.cassette

import com.dena.autum_hackathon_b.cassette.entity.Cassette
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CassetteRepositoryImpl @Inject constructor() : CassetteRepository {
    override suspend fun getCassette(id: String): Cassette {
        return  Cassette(id = id)
    }
}