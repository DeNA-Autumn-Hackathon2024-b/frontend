package com.dena.autum_hackathon_b.cassette.entity

data class Cassette(
    val id: String
) {
    data class Song(
        val name: String,
        val cachedAudioFile: CachedAudioFile,
    )
}