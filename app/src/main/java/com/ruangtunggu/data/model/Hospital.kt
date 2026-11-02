package com.ruangtunggu.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Hospital(
    val id: String = "",
    val nama: String = "",
    val alamat: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val rating: Float = 0f,
    @SerialName("image_url")
    val imageUrl: String? = null
)
