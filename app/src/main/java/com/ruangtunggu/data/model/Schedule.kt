package com.ruangtunggu.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Schedule(
    val id: String = "",
    @SerialName("doctor_id")
    val doctorId: String = "",
    val hari: String = "",
    @SerialName("jam_mulai")
    val jamMulai: String = "",
    @SerialName("jam_selesai")
    val jamSelesai: String = "",
    @SerialName("max_slot")
    val maxSlot: Int = 0,
    @SerialName("current_slot")
    val currentSlot: Int = 0
) {
    val isAvailable: Boolean get() = currentSlot < maxSlot
    val remainingSlot: Int get() = maxSlot - currentSlot
}
