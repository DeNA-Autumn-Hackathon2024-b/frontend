package com.dena.autum_hackathon_b.cassette.entity

import java.io.File
import java.io.Serializable

data class CachedAudioFile(
    val audioFile: File,
    val mimeType: String,
    val fileName: String
) : Serializable