package com.ruangtunggu.data.repository

import com.ruangtunggu.data.model.Notification
import com.ruangtunggu.data.model.NotificationType

interface NotificationRepository {
    suspend fun getNotifications(
        userId: String,
        type: NotificationType? = null
    ): Result<List<Notification>>
    suspend fun markAsRead(notificationId: String): Result<Unit>
    suspend fun markAllAsRead(userId: String): Result<Unit>
    suspend fun getUnreadCount(userId: String): Result<Int>
}
