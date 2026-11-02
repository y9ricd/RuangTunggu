package com.ruangtunggu.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Queue(
    val id: String = "",
    @SerialName("registration_id")
    val registrationId: String = "",
    @SerialName("nomor_antrean")
    val nomorAntrean: String = "",
    @SerialName("nomor_dilayani")
    val nomorDilayani: String = "",
    val status: QueueStatus = QueueStatus.WAITING,
    @SerialName("sisa_antrean")
    val sisaAntrean: Int = 0,
    @SerialName("estimasi_menit")
    val estimasiMenit: Int = 0,
    @SerialName("updated_at")
    val updatedAt: String = ""
)

@Serializable
enum class QueueStatus {
    @SerialName("waiting")
    WAITING,
    @SerialName("in_progress")
    IN_PROGRESS,
    @SerialName("completed")
    COMPLETED,
    @SerialName("paused")
    PAUSED,
    @SerialName("cancelled")
    CANCELLED
}
