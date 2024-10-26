package com.dena.autum_hackathon_b.cassette.data

import com.dena.autum_hackathon_b.cassette.entity.Cassette

interface CassetteRepository {
    suspend fun getCassette(id: String): Cassette
}