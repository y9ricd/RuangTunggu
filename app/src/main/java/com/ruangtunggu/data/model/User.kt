package com.ruangtunggu.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = "",
    val nik: String = "",
    val email: String = "",
    val nama: String = "",
    @SerialName("no_hp")
    val noHp: String = "",
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    @SerialName("created_at")
    val createdAt: String = ""
)
