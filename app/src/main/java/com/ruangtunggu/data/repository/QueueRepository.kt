package com.ruangtunggu.data.repository

import com.ruangtunggu.data.model.Queue
import com.ruangtunggu.data.model.Registration
import com.ruangtunggu.data.model.RegistrationStatus
import kotlinx.coroutines.flow.Flow

interface QueueRepository {
    suspend fun register(scheduleId: String): Result<Registration>
    suspend fun getActiveQueue(userId: String): Result<Queue?>
    suspend fun getQueueStatus(registrationId: String): Result<Queue>
    suspend fun cancelQueue(registrationId: String): Result<Unit>
    suspend fun rescheduleQueue(registrationId: String, newScheduleId: String): Result<Unit>
    fun observeQueueUpdates(registrationId: String): Flow<Queue>
    suspend fun getHistory(
        userId: String,
        status: RegistrationStatus? = null
    ): Result<List<Registration>>
}
