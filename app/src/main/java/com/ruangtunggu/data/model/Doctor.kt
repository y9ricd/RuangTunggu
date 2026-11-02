package com.ruangtunggu.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Doctor(
    val id: String = "",
    @SerialName("polyclinic_id")
    val polyclinicId: String = "",
    val nama: String = "",
    val spesialisasi: String = "",
    @SerialName("foto_url")
    val fotoUrl: String? = null
)
