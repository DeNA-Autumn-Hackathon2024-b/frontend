package com.dena.autum_hackathon_b.cassette.data.cassette.reset_api

import com.dena.autum_hackathon_b.cassette.entity.Cassette
import retrofit2.Retrofit
import retrofit2.http.GET
import javax.inject.Inject

class CassetteRestApi @Inject constructor(retrofit: Retrofit) {
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
}