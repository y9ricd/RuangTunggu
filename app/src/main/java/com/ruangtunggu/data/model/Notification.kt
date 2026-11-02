package com.ruangtunggu.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val id: String = "",
    @SerialName("user_id")
    val userId: String = "",
    val judul: String = "",
    val pesan: String = "",
    val tipe: NotificationType = NotificationType.INFO,
    @SerialName("is_read")
    val isRead: Boolean = false,
    @SerialName("created_at")
    val createdAt: String = ""
)

@Serializable
enum class NotificationType {
    @SerialName("queue")
    QUEUE,
    @SerialName("info")
    INFO,
    @SerialName("update")
    UPDATE
}
