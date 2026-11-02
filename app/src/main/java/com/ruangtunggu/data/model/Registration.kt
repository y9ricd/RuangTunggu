package com.ruangtunggu.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Registration(
    val id: String = "",
    @SerialName("user_id")
    val userId: String = "",
    @SerialName("schedule_id")
    val scheduleId: String = "",
    @SerialName("nomor_antrean")
    val nomorAntrean: String = "",
    val status: RegistrationStatus = RegistrationStatus.ACTIVE,
    @SerialName("created_at")
    val createdAt: String = ""
)

@Serializable
enum class RegistrationStatus {
    @SerialName("active")
    ACTIVE,
    @SerialName("completed")
    COMPLETED,
    @SerialName("cancelled")
    CANCELLED,
    @SerialName("rescheduled")
    RESCHEDULED
}
