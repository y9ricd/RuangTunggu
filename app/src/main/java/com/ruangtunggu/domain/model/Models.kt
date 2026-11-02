package com.ruangtunggu.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PatientProfile(
    val id: String = "usr-001",
    @SerialName("full_name") val fullName: String = "Nama Pasien",
    val initials: String = "NP",
    val nik: String = "3515081201950001",
    @SerialName("birth_place_date") val birthPlaceDate: String = "01 Januari 1995",
    val gender: String = "Laki-laki",
    val phone: String = "081234567890",
    val email: String = "email@contoh.com",
    @SerialName("is_verified") val isVerified: Boolean = true
) {
    val maskedNik: String
        get() = if (nik.length >= 4) "${nik.take(4)}••••••••••••" else "3515••••••••••••"
}

/**
 * 4-Phase Hospital Queue State Machine:
 * - Fase 1: MENUNGGU (Sisa > 3 antrean)
 * - Fase 2: PERINGATAN_DEKAT (Sisa <= 3 antrean, sesuai Detail_Giliran_Anda_Dekat)
 * - Fase 3: DIPANGGIL (Nomor Anda == Nomor yang Dilayani, sesuai DL01_Datang_Ke_Loket & DL02_Tunjukkan_Tiket)
 * - Fase 4: DIJEDA (Dokter Istirahat / Tindakan Darurat, sesuai Antrean_Dijeda)
 */
@Serializable
enum class QueuePhase {
    MENUNGGU,
    PERINGATAN_DEKAT,
    DIPANGGIL,
    DIJEDA
}

@Serializable
enum class QueueStatus {
    ACTIVE,
    PAUSED,
    COMPLETED,
    CANCELLED
}

@Serializable
data class ActiveQueue(
    val id: String = "q-a024",
    @SerialName("user_queue_number") val userQueueNumber: String = "A-024",
    @SerialName("current_served_number") val currentServedNumber: String = "A-019",
    @SerialName("poli_name") val poliName: String = "Poli Anak",
    @SerialName("doctor_name") val doctorName: String = "dr. Abdul, Sp.A",
    @SerialName("hospital_name") val hospitalName: String = "RS Sehat Sidoarjo",
    @SerialName("hospital_address") val hospitalAddress: String = "Jl. Contoh No. 1, Sidoarjo",
    @SerialName("schedule_date_time") val scheduleDateTime: String = "Kam, 22 Okt · 08:00",
    @SerialName("estimated_serve_clock") val estimatedServeClock: String = "08:30",
    @SerialName("remaining_queues") val remainingQueues: Int = 5,
    @SerialName("avg_consult_minutes_min") val avgConsultMinutesMin: Int = 5,
    @SerialName("avg_consult_minutes_max") val avgConsultMinutesMax: Int = 6,
    val progress: Float = 0.62f,
    val phase: QueuePhase = QueuePhase.MENUNGGU,
    val status: QueueStatus = QueueStatus.ACTIVE,
    @SerialName("upcoming_sequence") val upcomingSequence: List<String> = listOf(
        "A-020", "A-021", "A-022", "A-023", "A-024"
    ),
    @SerialName("qr_payload") val qrPayload: String = "RT-QR|A-024|RS Sehat Sidoarjo|Poli Anak|dr. Abdul, Sp.A|Kam, 22 Okt 08:00",
    @SerialName("is_offline_cached") val isOfflineCached: Boolean = true
) {
    /**
     * Rumus Estimasi Waktu Tunggu Dinamis (#5):
     * Estimasi Waktu = Sisa Antrean * Rata-rata Waktu Konsultasi Poli (5–6 menit).
     * Contoh: Sisa 4 orang -> "± 20–25 menit".
     */
    val dynamicWaitRangeText: String
        get() = when {
            phase == QueuePhase.DIJEDA -> "Dijeda ± 15 menit (Visite/Darurat)"
            phase == QueuePhase.DIPANGGIL || remainingQueues <= 0 -> "Giliran Anda sekarang!"
            else -> {
                val minWait = remainingQueues * avgConsultMinutesMin
                val maxWait = remainingQueues * avgConsultMinutesMax + (if (remainingQueues == 4) 1 else 0)
                "± $minWait–$maxWait menit"
            }
        }

    val dynamicWaitSummary: String
        get() = when (phase) {
            QueuePhase.DIJEDA -> "Pelayanan dijeda · nomor Anda tetap aman"
            QueuePhase.DIPANGGIL -> "Silakan masuk ke ruang periksa / loket"
            else -> "Sisa $remainingQueues antrean · $dynamicWaitRangeText"
        }
}

@Serializable
data class CompletedConsultation(
    val queueNumber: String,
    val hospitalName: String,
    val poliName: String,
    val doctorName: String,
    val visitDate: String = "Kam, 22 Okt 2026"
)

@Serializable
data class HospitalItem(
    val id: String,
    val name: String,
    val address: String = "Jl. Contoh No. 1, Sidoarjo",
    val rating: Double,
    val distanceKm: String,
    val openHours: String = "Buka 24 jam",
    val availablePoli: List<String> = listOf("Anak", "Umum", "Gigi"),
    val drawableResName: String
)

@Serializable
data class PoliItem(
    val id: String,
    val name: String,
    val shortName: String,
    val doctorCount: Int,
    val startHour: String,
    val avgConsultMinutes: Int = 5
)

@Serializable
data class DoctorTimeSlot(
    val time: String,
    val isFull: Boolean = false,
    val currentBooked: Int = 18,
    val maxQuota: Int = 30
) {
    val quotaLabel: String
        get() = "$currentBooked/$maxQuota"
}

@Serializable
data class DoctorScheduleItem(
    val id: String,
    val name: String,
    val initials: String,
    val specialty: String,
    val poliShortName: String,
    val rating: Double,
    val experienceYears: Int,
    val slots: List<DoctorTimeSlot>
)

@Serializable
data class QueueHistoryItem(
    val id: String,
    val hospitalName: String,
    val poliAndDate: String,
    val doctorName: String = "dr. Abdul, Sp.A",
    val queueNumber: String,
    val remainingInfo: String,
    val subStatusInfo: String,
    val progress: Float,
    val status: QueueStatus
)

@Serializable
data class ChatMessageItem(
    val id: String,
    val senderName: String,
    val message: String,
    val time: String,
    val isFromUser: Boolean
)

@Serializable
data class NotificationItem(
    val id: String,
    val title: String,
    val body: String,
    val timeAgo: String,
    val category: String, // "ANTREAN" or "INFO"
    val isUnread: Boolean = true,
    val dayGroup: String = "Hari ini"
)

@Serializable
data class ReminderToggleItem(
    val id: String,
    val title: String,
    val scheduleLabel: String,
    val isEnabled: Boolean
)
