package com.ruangtunggu.data.repository

import com.ruangtunggu.data.model.Queue
import com.ruangtunggu.data.model.QueueStatus
import com.ruangtunggu.data.model.Registration
import com.ruangtunggu.data.model.RegistrationStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QueueRepositoryImpl @Inject constructor() : QueueRepository {

    private var activeQueue: Queue? = Queue(
        id = "queue_001",
        registrationId = "reg_001",
        nomorAntrean = "A-023",
        nomorDilayani = "A-018",
        status = QueueStatus.WAITING,
        sisaAntrean = 5,
        estimasiMenit = 25,
        updatedAt = "2026-10-22 09:00"
    )

    override suspend fun register(scheduleId: String): Result<Registration> {
        delay(600)
        val reg = Registration(
            id = "reg_new",
            userId = "user_01",
            scheduleId = scheduleId,
            nomorAntrean = "A-024",
            status = RegistrationStatus.ACTIVE,
            createdAt = "2026-11-02"
        )
        return Result.success(reg)
    }

    override suspend fun getActiveQueue(userId: String): Result<Queue?> {
        delay(300)
        return Result.success(activeQueue)
    }

    override suspend fun getQueueStatus(registrationId: String): Result<Queue> {
        delay(300)
        return activeQueue?.let { Result.success(it) }
            ?: Result.failure(Exception("Antrean tidak ditemukan"))
    }

    override suspend fun cancelQueue(registrationId: String): Result<Unit> {
        delay(400)
        activeQueue = null
        return Result.success(Unit)
    }

    override suspend fun rescheduleQueue(registrationId: String, newScheduleId: String): Result<Unit> {
        delay(500)
        return Result.success(Unit)
    }

    override fun observeQueueUpdates(registrationId: String): Flow<Queue> = flow {
        activeQueue?.let { emit(it) }
    }

    override suspend fun getHistory(userId: String, status: RegistrationStatus?): Result<List<Registration>> {
        delay(400)
        val list = listOf(
            Registration(
                id = "reg_hist_1",
                userId = userId,
                scheduleId = "sch_1",
                nomorAntrean = "A-012",
                status = RegistrationStatus.COMPLETED,
                createdAt = "15 Okt 2026"
            ),
            Registration(
                id = "reg_hist_2",
                userId = userId,
                scheduleId = "sch_2",
                nomorAntrean = "G-005",
                status = RegistrationStatus.COMPLETED,
                createdAt = "02 Okt 2026"
            )
        )
        return Result.success(list)
    }
}

