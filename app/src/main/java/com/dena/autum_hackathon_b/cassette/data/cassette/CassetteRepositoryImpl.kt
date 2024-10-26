package com.dena.autum_hackathon_b.cassette.data.cassette

import com.dena.autum_hackathon_b.cassette.data.cassette.reset_api.CassetteRestApi
import com.dena.autum_hackathon_b.cassette.entity.Cassette
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CassetteRepositoryImpl @Inject constructor(
    private val cassetteRestApi: CassetteRestApi
) : CassetteRepository {
    override suspend fun getCassette(id: String): Cassette {
        cassetteRestApi
        return Cassette(id = id)
    }
}