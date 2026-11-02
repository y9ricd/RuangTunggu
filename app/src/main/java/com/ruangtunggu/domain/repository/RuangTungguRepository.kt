package com.ruangtunggu.domain.repository

import com.ruangtunggu.domain.model.ActiveQueue
import com.ruangtunggu.domain.model.ChatMessageItem
import com.ruangtunggu.domain.model.CompletedConsultation
import com.ruangtunggu.domain.model.DoctorScheduleItem
import com.ruangtunggu.domain.model.HospitalItem
import com.ruangtunggu.domain.model.NotificationItem
import com.ruangtunggu.domain.model.PatientProfile
import com.ruangtunggu.domain.model.PoliItem
import com.ruangtunggu.domain.model.QueueHistoryItem
import com.ruangtunggu.domain.model.QueuePhase
import com.ruangtunggu.domain.model.ReminderToggleItem
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val currentProfileFlow: StateFlow<PatientProfile>
    suspend fun login(nikOrEmail: String, password: String): Result<PatientProfile>
    suspend fun register(
        fullName: String,
        emailOrPhone: String,
        nik: String,
        birthPlaceDate: String,
        password: String
    ): Result<PatientProfile>
    suspend fun requestPasswordResetOtp(nikOrEmail: String): Result<String>
    suspend fun confirmPasswordReset(nikOrEmail: String, otpCode: String, newPassword: String): Result<Unit>
    suspend fun updateProfile(
        fullName: String,
        nik: String,
        birthPlaceDate: String,
        gender: String,
        phone: String
    ): Result<PatientProfile>
    suspend fun getCurrentProfile(): PatientProfile
    suspend fun logout()
}

interface QueueRepository {
    val activeQueueFlow: StateFlow<ActiveQueue?>
    val completedConsultationFlow: StateFlow<CompletedConsultation?>
    val historyFlow: StateFlow<List<QueueHistoryItem>>
    val notificationsFlow: StateFlow<List<NotificationItem>>
    val chatMessagesFlow: StateFlow<List<ChatMessageItem>>
    val remindersFlow: StateFlow<List<ReminderToggleItem>>

    fun getOfflineCachedTicket(): ActiveQueue?
    suspend fun clearForFreshAccount()

    suspend fun bookNewQueue(
        hospital: HospitalItem,
        poli: PoliItem,
        doctor: DoctorScheduleItem,
        dateLabel: String,
        slotTime: String
    ): ActiveQueue

    suspend fun advanceQueueSimulation(): ActiveQueue?
    suspend fun setQueuePhase(phase: QueuePhase)
    suspend fun cancelActiveQueue(reason: String)
    suspend fun rescheduleActiveQueue(newDateLabel: String, newSlotTime: String): ActiveQueue?
    suspend fun completeActiveQueueForReview()
    suspend fun submitConsultationReview(
        stars: Int,
        tags: List<String>,
        comment: String,
        isAnonymous: Boolean
    )
    suspend fun dismissConsultationReview()

    suspend fun getPopularPoli(): List<String>
    suspend fun getAvailablePoliList(): List<PoliItem>
    suspend fun getNearbyHospitals(query: String = "", poliFilter: String = "Semua"): List<HospitalItem>
    suspend fun getDoctorsForPoli(poliShortName: String, dateIndex: Int = 0): List<DoctorScheduleItem>

    suspend fun addSystemAndInAppNotification(title: String, body: String, category: String = "INFO")
    suspend fun sendChatMessage(text: String)
    suspend fun markAllNotificationsRead()
    suspend fun toggleReminder(id: String)
}
