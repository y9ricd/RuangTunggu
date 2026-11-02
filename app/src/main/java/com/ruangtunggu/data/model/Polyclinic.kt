package com.ruangtunggu.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Polyclinic(
    val id: String = "",
    @SerialName("hospital_id")
    val hospitalId: String = "",
    val nama: String = "",
    val icon: String = ""
)
