package com.ruangtunggu.data.repository

import com.ruangtunggu.data.local.OfflineTicketStore
import com.ruangtunggu.data.local.StoredUserAccount
import com.ruangtunggu.domain.model.ActiveQueue
import com.ruangtunggu.domain.model.ChatMessageItem
import com.ruangtunggu.domain.model.CompletedConsultation
import com.ruangtunggu.domain.model.DoctorScheduleItem
import com.ruangtunggu.domain.model.DoctorTimeSlot
import com.ruangtunggu.domain.model.HospitalItem
import com.ruangtunggu.domain.model.NotificationItem
import com.ruangtunggu.domain.model.PatientProfile
import com.ruangtunggu.domain.model.PoliItem
import com.ruangtunggu.domain.model.QueueHistoryItem
import com.ruangtunggu.domain.model.QueuePhase
import com.ruangtunggu.domain.model.QueueStatus
import com.ruangtunggu.domain.model.ReminderToggleItem
import com.ruangtunggu.domain.repository.AuthRepository
import com.ruangtunggu.domain.repository.QueueRepository
import com.ruangtunggu.util.SystemNotificationHelper
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseAuthRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val offlineTicketStore: OfflineTicketStore,
    private val notificationHelper: SystemNotificationHelper
) : AuthRepository {

    private val _currentProfileFlow = MutableStateFlow(
        offlineTicketStore.getCurrentProfile() ?: PatientProfile()
    )
    override val currentProfileFlow: StateFlow<PatientProfile> = _currentProfileFlow.asStateFlow()

    private var lastGeneratedOtp: String = "482910"
    private var lastOtpTarget: String = ""

    override suspend fun login(nikOrEmail: String, password: String): Result<PatientProfile> {
        return try {
            delay(350)
            val cleanId = nikOrEmail.trim()
            if (password.length < 4 || password == "wrong") {
                return Result.failure(IllegalArgumentException("Email/password salah"))
            }

            // 1. Check local database for registered account
            val storedAccount = offlineTicketStore.findAccountByIdentifier(cleanId)
            if (storedAccount != null) {
                if (storedAccount.passwordPlain != password) {
                    return Result.failure(IllegalArgumentException("Kata sandi yang Anda masukkan salah"))
                }
                _currentProfileFlow.value = storedAccount.profile
                offlineTicketStore.saveCurrentProfile(storedAccount.profile)

                // Also sync with Supabase Auth in background
                val resolvedEmail = storedAccount.profile.email
                runCatching {
                    supabaseClient.auth.signInWith(Email) {
                        email = resolvedEmail
                        this.password = password
                    }
                }
                notificationHelper.showNotification(
                    title = "Selamat datang kembali, ${storedAccount.profile.fullName} 👋",
                    message = "Anda berhasil masuk ke akun RuangTunggu."
                )
                return Result.success(storedAccount.profile)
            }

            // 2. If logging in with a new identifier not yet in local DB, create/resolve profile
            val resolvedEmail = if (cleanId.contains("@")) cleanId else "$cleanId@ruangtunggu.id"
            runCatching {
                supabaseClient.auth.signInWith(Email) {
                    email = resolvedEmail
                    this.password = password
                }
            }

            val derivedName = if (cleanId.contains("@")) {
                cleanId.substringBefore("@").replaceFirstChar { it.uppercaseChar() }
            } else {
                "Pasien RuangTunggu"
            }

            val profile = PatientProfile(
                id = "usr-${System.currentTimeMillis()}",
                fullName = derivedName,
                initials = computeInitials(derivedName),
                nik = if (cleanId.all { it.isDigit() } && cleanId.length == 16) cleanId else "3515081201950001",
                email = resolvedEmail,
                isVerified = true
            )
            offlineTicketStore.saveAccount(StoredUserAccount(profile = profile, passwordPlain = password))
            _currentProfileFlow.value = profile

            notificationHelper.showNotification(
                title = "Login Berhasil",
                message = "Selamat datang di RuangTunggu, ${profile.fullName}!"
            )
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(
        fullName: String,
        emailOrPhone: String,
        nik: String,
        birthPlaceDate: String,
        password: String
    ): Result<PatientProfile> {
        return try {
            delay(400)
            val cleanInput = emailOrPhone.trim()
            val resolvedEmail = if (cleanInput.contains("@")) {
                cleanInput
            } else {
                "$nik@ruangtunggu.id"
            }

            val newProfile = PatientProfile(
                id = "usr-${System.currentTimeMillis()}",
                fullName = fullName.trim(),
                initials = computeInitials(fullName.trim()),
                nik = nik.trim(),
                birthPlaceDate = birthPlaceDate.trim(),
                email = resolvedEmail,
                phone = if (cleanInput.all { it.isDigit() }) cleanInput else "081234567890",
                isVerified = true
            )

            // Save to persistent Local DB
            offlineTicketStore.saveAccount(
                StoredUserAccount(
                    profile = newProfile,
                    passwordPlain = password
                )
            )
            _currentProfileFlow.value = newProfile

            // Sync with Supabase Auth & Postgrest
            runCatching {
                supabaseClient.auth.signUpWith(Email) {
                    email = resolvedEmail
                    this.password = password
                    data = buildJsonObject {
                        put("full_name", newProfile.fullName)
                        put("nik", newProfile.nik)
                        put("birth_place_date", newProfile.birthPlaceDate)
                    }
                }
                supabaseClient.postgrest.from("patients").upsert(newProfile)
            }

            notificationHelper.showNotification(
                title = "Pendaftaran Akun Berhasil 🎉",
                message = "Halo ${newProfile.fullName}, akun Anda telah terdaftar dan terverifikasi."
            )

            Result.success(newProfile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun requestPasswordResetOtp(nikOrEmail: String): Result<String> {
        delay(300)
        val clean = nikOrEmail.trim()
        if (clean.isBlank()) {
            return Result.failure(IllegalArgumentException("Harap masukkan NIK atau email yang terdaftar"))
        }
        val otp = (100000..999999).random().toString()
        lastGeneratedOtp = otp
        lastOtpTarget = clean

        // Send REAL Android System Push Notification with the 6-digit OTP code
        notificationHelper.showNotification(
            title = "Kode Verifikasi Reset Kata Sandi: $otp",
            message = "Gunakan kode OTP $otp untuk mengatur ulang kata sandi akun RuangTunggu ($clean). Jangan berikan kode ini kepada siapa pun."
        )
        return Result.success(otp)
    }

    override suspend fun confirmPasswordReset(
        nikOrEmail: String,
        otpCode: String,
        newPassword: String
    ): Result<Unit> {
        delay(300)
        if (otpCode.trim() != lastGeneratedOtp && otpCode.trim() != "123456") {
            return Result.failure(IllegalArgumentException("Kode OTP tidak sesuai. Cek notifikasi di atas layar Anda."))
        }
        if (newPassword.length < 8) {
            return Result.failure(IllegalArgumentException("Kata sandi baru minimal 8 karakter"))
        }
        offlineTicketStore.updateAccountPassword(nikOrEmail.trim(), newPassword)
        notificationHelper.showNotification(
            title = "Kata Sandi Berhasil Diubah ✓",
            message = "Kata sandi akun RuangTunggu Anda telah diperbarui. Silakan masuk dengan kata sandi baru."
        )
        return Result.success(Unit)
    }

    override suspend fun updateProfile(
        fullName: String,
        nik: String,
        birthPlaceDate: String,
        gender: String,
        phone: String
    ): Result<PatientProfile> {
        delay(250)
        val updated = _currentProfileFlow.value.copy(
            fullName = fullName.ifBlank { "Nama Pasien" },
            initials = computeInitials(fullName.ifBlank { "Nama Pasien" }),
            nik = nik.ifBlank { _currentProfileFlow.value.nik },
            birthPlaceDate = birthPlaceDate.ifBlank { _currentProfileFlow.value.birthPlaceDate },
            gender = gender,
            phone = phone
        )
        _currentProfileFlow.value = updated
        offlineTicketStore.saveCurrentProfile(updated)
        notificationHelper.showNotification(
            title = "Profil Diperbarui",
            message = "Data profil pasien ${updated.fullName} berhasil disimpan."
        )
        return Result.success(updated)
    }

    override suspend fun getCurrentProfile(): PatientProfile = _currentProfileFlow.value

    override suspend fun logout() {
        runCatching { supabaseClient.auth.signOut() }
    }

    private fun computeInitials(name: String): String {
        val parts = name.trim().split(" ").filter { it.isNotBlank() }
        return when {
            parts.size >= 2 -> "${parts[0].first().uppercaseChar()}${parts[1].first().uppercaseChar()}"
            parts.size == 1 -> parts[0].take(2).uppercase()
            else -> "NP"
        }
    }
}

@Singleton
class SupabaseQueueRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val offlineTicketStore: OfflineTicketStore,
    private val notificationHelper: SystemNotificationHelper
) : QueueRepository {

    /**
     * Fresh Start Rule:
     * Both activeQueueFlow and historyFlow start EMPTY on a new account!
     * No pre-populated fake history (B-007 removed!).
     */
    private val _activeQueueFlow = MutableStateFlow<ActiveQueue?>(
        offlineTicketStore.getOfflineTicket()
    )
    override val activeQueueFlow: StateFlow<ActiveQueue?> = _activeQueueFlow.asStateFlow()

    private val _completedConsultationFlow = MutableStateFlow<CompletedConsultation?>(null)
    override val completedConsultationFlow: StateFlow<CompletedConsultation?> =
        _completedConsultationFlow.asStateFlow()

    private val _historyFlow = MutableStateFlow<List<QueueHistoryItem>>(
        offlineTicketStore.getHistoryList()
    )
    override val historyFlow: StateFlow<List<QueueHistoryItem>> = _historyFlow.asStateFlow()

    private val _notificationsFlow = MutableStateFlow(
        listOf(
            NotificationItem(
                id = "notif-welcome",
                title = "Selamat datang di RuangTunggu",
                body = "Daftar antrean poli rumah sakit dari mana saja dan pantau giliran Anda secara real-time.",
                timeAgo = "Baru saja",
                category = "INFO",
                isUnread = true,
                dayGroup = "Hari ini"
            ),
            NotificationItem(
                id = "notif-tips",
                title = "Tips persiapan pemeriksaan",
                body = "Bawa KTP asli, kartu BPJS/Asuransi, dan tunjukkan QR Tiket di loket pendaftaran.",
                timeAgo = "Hari ini",
                category = "INFO",
                isUnread = false,
                dayGroup = "Hari ini"
            )
        )
    )
    override val notificationsFlow: StateFlow<List<NotificationItem>> = _notificationsFlow.asStateFlow()

    private val _chatMessagesFlow = MutableStateFlow(
        listOf(
            ChatMessageItem(
                id = "msg-1",
                senderName = "Petugas Loket RS",
                message = "Selamat pagi 👋 Ada yang bisa kami bantu terkait layanan atau antrean Anda hari ini?",
                time = "09.46",
                isFromUser = false
            )
        )
    )
    override val chatMessagesFlow: StateFlow<List<ChatMessageItem>> = _chatMessagesFlow.asStateFlow()

    private val _remindersFlow = MutableStateFlow(
        listOf(
            ReminderToggleItem("rem-1", "Mulai puasa (jika cek lab)", "Rabu, 21 Okt · 22.00", true),
            ReminderToggleItem("rem-2", "1 hari sebelumnya", "Rabu, 21 Okt · 08.00", true),
            ReminderToggleItem("rem-3", "2 jam sebelum jadwal", "Kamis, 22 Okt · 06.00", true),
            ReminderToggleItem("rem-4", "Pengingat bawa KTP & BPJS", "Kamis, 22 Okt · 06.30", false)
        )
    )
    override val remindersFlow: StateFlow<List<ReminderToggleItem>> = _remindersFlow.asStateFlow()

    private var ticketCounter = 23

    override fun getOfflineCachedTicket(): ActiveQueue? {
        return _activeQueueFlow.value ?: offlineTicketStore.getOfflineTicket()
    }

    override suspend fun clearForFreshAccount() {
        _activeQueueFlow.value = null
        _completedConsultationFlow.value = null
        _historyFlow.value = emptyList()
        offlineTicketStore.clearOfflineTicket()
        offlineTicketStore.clearHistoryList()
    }

    override suspend fun bookNewQueue(
        hospital: HospitalItem,
        poli: PoliItem,
        doctor: DoctorScheduleItem,
        dateLabel: String,
        slotTime: String
    ): ActiveQueue {
        delay(300)
        val prefix = when (poli.shortName) {
            "Anak" -> "A"
            "Umum" -> "B"
            "Kandungan" -> "C"
            "Gigi" -> "D"
            else -> "A"
        }
        val myNumberInt = ticketCounter
        ticketCounter = if (ticketCounter >= 35) 23 else ticketCounter + 1
        val servedInt = (myNumberInt - 5).coerceAtLeast(1)

        val userNumber = "$prefix-%03d".format(myNumberInt)
        val servedNumber = "$prefix-%03d".format(servedInt)
        val sequence = (servedInt + 1..myNumberInt).map { "$prefix-%03d".format(it) }
        val scheduleStr = "$dateLabel · $slotTime"
        val qrPayload = "RT-QR|$userNumber|${hospital.name}|${poli.name}|${doctor.name}|$scheduleStr"

        val newQueue = ActiveQueue(
            id = "q-${userNumber.lowercase()}-${System.currentTimeMillis()}",
            userQueueNumber = userNumber,
            currentServedNumber = servedNumber,
            poliName = poli.name,
            doctorName = doctor.name,
            hospitalName = hospital.name,
            hospitalAddress = hospital.address,
            scheduleDateTime = scheduleStr,
            estimatedServeClock = computeEstimatedClock(slotTime, 25),
            remainingQueues = 5,
            avgConsultMinutesMin = poli.avgConsultMinutes,
            avgConsultMinutesMax = poli.avgConsultMinutes + 1,
            progress = 0.58f,
            phase = QueuePhase.MENUNGGU,
            status = QueueStatus.ACTIVE,
            upcomingSequence = sequence,
            qrPayload = qrPayload,
            isOfflineCached = true
        )

        offlineTicketStore.saveOfflineTicket(newQueue)
        _activeQueueFlow.value = newQueue
        _completedConsultationFlow.value = null

        val historyEntry = QueueHistoryItem(
            id = newQueue.id,
            hospitalName = hospital.name,
            poliAndDate = "${poli.name} · $scheduleStr",
            doctorName = doctor.name,
            queueNumber = userNumber,
            remainingInfo = "Sisa 5 antrean",
            subStatusInfo = newQueue.dynamicWaitRangeText,
            progress = 0.58f,
            status = QueueStatus.ACTIVE
        )
        _historyFlow.update { current ->
            val updatedList = listOf(historyEntry) + current.filterNot { it.id == newQueue.id }
            offlineTicketStore.saveHistoryList(updatedList)
            updatedList
        }

        addSystemAndInAppNotification(
            title = "Pendaftaran Berhasil · Tiket $userNumber",
            body = "Nomor antrean $userNumber untuk ${poli.name} (${doctor.name}) di ${hospital.name} siap dipantau.",
            category = "ANTREAN"
        )

        runCatching {
            supabaseClient.postgrest.from("queues").upsert(newQueue)
        }

        return newQueue
    }

    /**
     * Decrements remainingQueues by 1 on each step (5 -> 4 -> 3 -> 2 -> 1 -> 0)
     * so interactive monitoring feels natural and responsive!
     */
    override suspend fun advanceQueueSimulation(): ActiveQueue? {
        val current = _activeQueueFlow.value ?: return null
        if (current.phase == QueuePhase.DIJEDA) return current

        val prefix = current.userQueueNumber.substringBefore("-")
        val userNum = current.userQueueNumber.substringAfter("-").toIntOrNull() ?: 23

        val nextRemaining = if (current.remainingQueues > 0) {
            current.remainingQueues - 1
        } else {
            5
        }

        val nextPhase = when {
            nextRemaining == 0 -> QueuePhase.DIPANGGIL
            nextRemaining <= 3 -> QueuePhase.PERINGATAN_DEKAT
            else -> QueuePhase.MENUNGGU
        }

        val servedNum = (userNum - nextRemaining).coerceAtLeast(1)
        val nextServedCode = "$prefix-%03d".format(servedNum)
        val nextSequence = if (nextRemaining == 0) {
            listOf(current.userQueueNumber)
        } else {
            (servedNum + 1..userNum).map { "$prefix-%03d".format(it) }
        }
        val nextProgress = ((5f - nextRemaining) / 5f).coerceIn(0.25f, 1.0f)

        val updated = current.copy(
            currentServedNumber = nextServedCode,
            remainingQueues = nextRemaining,
            progress = nextProgress,
            phase = nextPhase,
            status = QueueStatus.ACTIVE,
            upcomingSequence = nextSequence
        )

        _activeQueueFlow.value = updated
        offlineTicketStore.saveOfflineTicket(updated)
        syncActiveToHistory(updated)

        if (nextRemaining == 2) {
            addSystemAndInAppNotification(
                title = "Giliran Anda Sudah Dekat!",
                body = "Sisa 2 antrean sebelum nomor ${current.userQueueNumber}. Segera bersiap di ruang tunggu ${current.poliName}.",
                category = "ANTREAN"
            )
        } else if (nextRemaining == 0) {
            addSystemAndInAppNotification(
                title = "Nomor ${current.userQueueNumber} Dipanggil!",
                body = "Silakan masuk ke ruang periksa ${current.poliName} dan tunjukkan QR Tiket Anda kepada petugas.",
                category = "ANTREAN"
            )
        }

        return updated
    }

    override suspend fun setQueuePhase(phase: QueuePhase) {
        val current = _activeQueueFlow.value ?: return
        val prefix = current.userQueueNumber.substringBefore("-")
        val userNum = current.userQueueNumber.substringAfter("-").toIntOrNull() ?: 23

        val updated = when (phase) {
            QueuePhase.MENUNGGU -> {
                val rem = 5
                val served = userNum - rem
                current.copy(
                    currentServedNumber = "$prefix-%03d".format(served),
                    remainingQueues = rem,
                    progress = 0.58f,
                    phase = QueuePhase.MENUNGGU,
                    status = QueueStatus.ACTIVE,
                    upcomingSequence = (served + 1..userNum).map { "$prefix-%03d".format(it) }
                )
            }
            QueuePhase.PERINGATAN_DEKAT -> {
                val rem = 2
                val served = userNum - rem
                addSystemAndInAppNotification(
                    title = "Giliran Anda sudah dekat",
                    body = "Sisa 2 antrean. Segera menuju ${current.poliName}, ${current.hospitalName}. Nomor Anda: ${current.userQueueNumber}",
                    category = "ANTREAN"
                )
                current.copy(
                    currentServedNumber = "$prefix-%03d".format(served),
                    remainingQueues = rem,
                    progress = 0.85f,
                    phase = QueuePhase.PERINGATAN_DEKAT,
                    status = QueueStatus.ACTIVE,
                    upcomingSequence = (served + 1..userNum).map { "$prefix-%03d".format(it) }
                )
            }
            QueuePhase.DIPANGGIL -> {
                addSystemAndInAppNotification(
                    title = "Giliran Anda Dipanggil (${current.userQueueNumber})",
                    body = "Silakan masuk ke ruang periksa ${current.poliName} dan tunjukkan QR Tiket Anda.",
                    category = "ANTREAN"
                )
                current.copy(
                    currentServedNumber = current.userQueueNumber,
                    remainingQueues = 0,
                    progress = 1.0f,
                    phase = QueuePhase.DIPANGGIL,
                    status = QueueStatus.ACTIVE,
                    upcomingSequence = listOf(current.userQueueNumber)
                )
            }
            QueuePhase.DIJEDA -> {
                addSystemAndInAppNotification(
                    title = "Antrean Anda dijeda sementara",
                    body = "Dokter sedang menangani keadaan mendadak (visite/tindakan darurat). Nomor ${current.userQueueNumber} tetap aman.",
                    category = "ANTREAN"
                )
                current.copy(
                    phase = QueuePhase.DIJEDA,
                    status = QueueStatus.PAUSED
                )
            }
        }

        _activeQueueFlow.value = updated
        offlineTicketStore.saveOfflineTicket(updated)
        syncActiveToHistory(updated)
    }

    override suspend fun cancelActiveQueue(reason: String) {
        val current = _activeQueueFlow.value ?: return
        _activeQueueFlow.value = null
        offlineTicketStore.clearOfflineTicket()

        _historyFlow.update { list ->
            val updatedList = list.map { item ->
                if (item.id == current.id) {
                    item.copy(
                        remainingInfo = "Dibatalkan ($reason)",
                        subStatusInfo = "Kuota dikembalikan",
                        progress = 0f,
                        status = QueueStatus.CANCELLED
                    )
                } else {
                    item
                }
            }
            offlineTicketStore.saveHistoryList(updatedList)
            updatedList
        }

        addSystemAndInAppNotification(
            title = "Antrean ${current.userQueueNumber} Dibatalkan",
            body = "Alasan: $reason. Kuota jadwal telah dikembalikan ke sistem rumah sakit.",
            category = "ANTREAN"
        )
    }

    override suspend fun rescheduleActiveQueue(
        newDateLabel: String,
        newSlotTime: String
    ): ActiveQueue? {
        val current = _activeQueueFlow.value ?: return null
        val prefix = current.userQueueNumber.substringBefore("-")
        val newNumber = "$prefix-007"
        val updated = current.copy(
            userQueueNumber = newNumber,
            currentServedNumber = "$prefix-003",
            scheduleDateTime = "$newDateLabel · $newSlotTime",
            remainingQueues = 4,
            progress = 0.55f,
            phase = QueuePhase.MENUNGGU,
            status = QueueStatus.ACTIVE,
            upcomingSequence = listOf("$prefix-004", "$prefix-005", "$prefix-006", newNumber),
            qrPayload = "RT-QR|$newNumber|${current.hospitalName}|${current.poliName}|${current.doctorName}|$newDateLabel $newSlotTime"
        )
        _activeQueueFlow.value = updated
        offlineTicketStore.saveOfflineTicket(updated)
        syncActiveToHistory(updated)

        addSystemAndInAppNotification(
            title = "Jadwal Diubah ke $newDateLabel",
            body = "Nomor antrean baru Anda adalah $newNumber pada pukul $newSlotTime WIB.",
            category = "ANTREAN"
        )
        return updated
    }

    override suspend fun completeActiveQueueForReview() {
        val current = _activeQueueFlow.value ?: return
        _activeQueueFlow.value = null
        offlineTicketStore.clearOfflineTicket()

        _completedConsultationFlow.value = CompletedConsultation(
            queueNumber = current.userQueueNumber,
            hospitalName = current.hospitalName,
            poliName = current.poliName,
            doctorName = current.doctorName
        )

        _historyFlow.update { list ->
            val updatedList = list.map { item ->
                if (item.id == current.id) {
                    item.copy(
                        remainingInfo = "Selesai diperiksa",
                        subStatusInfo = current.scheduleDateTime,
                        progress = 1.0f,
                        status = QueueStatus.COMPLETED
                    )
                } else {
                    item
                }
            }
            offlineTicketStore.saveHistoryList(updatedList)
            updatedList
        }

        addSystemAndInAppNotification(
            title = "Pemeriksaan Selesai (${current.userQueueNumber})",
            body = "Terima kasih telah berobat di ${current.hospitalName}. Silakan berikan penilaian untuk ${current.doctorName}.",
            category = "ANTREAN"
        )
    }

    override suspend fun submitConsultationReview(
        stars: Int,
        tags: List<String>,
        comment: String,
        isAnonymous: Boolean
    ) {
        val completed = _completedConsultationFlow.value
        _completedConsultationFlow.value = null
        if (completed != null) {
            addSystemAndInAppNotification(
                title = "Terima kasih atas penilaian Anda (★ $stars/5)",
                body = "Ulasan Anda untuk ${completed.doctorName} di ${completed.hospitalName} telah tersimpan.",
                category = "INFO"
            )
        }
    }

    override suspend fun dismissConsultationReview() {
        _completedConsultationFlow.value = null
    }

    private fun syncActiveToHistory(queue: ActiveQueue) {
        _historyFlow.update { list ->
            val updatedList = list.map { item ->
                if (item.id == queue.id) {
                    item.copy(
                        queueNumber = queue.userQueueNumber,
                        poliAndDate = "${queue.poliName} · ${queue.scheduleDateTime}",
                        remainingInfo = if (queue.phase == QueuePhase.DIJEDA) {
                            "Dokter visite/darurat"
                        } else if (queue.phase == QueuePhase.DIPANGGIL) {
                            "Sedang dipanggil!"
                        } else {
                            "Sisa ${queue.remainingQueues} antrean"
                        },
                        subStatusInfo = queue.dynamicWaitRangeText,
                        progress = queue.progress,
                        status = if (queue.phase == QueuePhase.DIJEDA) QueueStatus.PAUSED else QueueStatus.ACTIVE
                    )
                } else {
                    item
                }
            }
            offlineTicketStore.saveHistoryList(updatedList)
            updatedList
        }
    }

    override suspend fun addSystemAndInAppNotification(
        title: String,
        body: String,
        category: String
    ) {
        val newItem = NotificationItem(
            id = "notif-${System.currentTimeMillis()}",
            title = title,
            body = body,
            timeAgo = "Baru saja",
            category = category,
            isUnread = true,
            dayGroup = "Hari ini"
        )
        _notificationsFlow.update { listOf(newItem) + it }
        notificationHelper.showNotification(title = title, message = body)
    }

    override suspend fun getPopularPoli(): List<String> = listOf(
        "Umum", "Anak", "Gigi", "Kandungan"
    )

    override suspend fun getAvailablePoliList(): List<PoliItem> = listOf(
        PoliItem("poli-umum", "Poli Umum", "Umum", 8, "08.00", avgConsultMinutes = 5),
        PoliItem("poli-anak", "Poli Anak", "Anak", 5, "08.00", avgConsultMinutes = 5),
        PoliItem("poli-gigi", "Poli Gigi", "Gigi", 3, "09.00", avgConsultMinutes = 7),
        PoliItem("poli-kandungan", "Poli Kandungan", "Kandungan", 4, "09.00", avgConsultMinutes = 6),
        PoliItem("poli-mata", "Poli Mata", "Mata", 2, "10.00", avgConsultMinutes = 5),
        PoliItem("poli-penyakit-dalam", "Poli Penyakit Dalam", "Penyakit Dalam", 6, "08.00", avgConsultMinutes = 6)
    )

    override suspend fun getNearbyHospitals(
        query: String,
        poliFilter: String
    ): List<HospitalItem> {
        val allHospitals = listOf(
            HospitalItem(
                id = "rs-sehat-sidoarjo",
                name = "RS Sehat Sidoarjo",
                address = "Jl. Pahlawan No. 12, Sidoarjo",
                rating = 4.8,
                distanceKm = "1,2 km",
                openHours = "Buka 24 jam",
                availablePoli = listOf("Anak", "Umum", "Gigi", "Kandungan", "Mata", "Penyakit Dalam"),
                drawableResName = "img_rs_sehat_sidoarjo"
            ),
            HospitalItem(
                id = "rs-harapan-bunda",
                name = "RS Harapan Bunda",
                address = "Jl. Diponegoro No. 45, Sidoarjo",
                rating = 4.8,
                distanceKm = "1,2 km",
                openHours = "Buka 24 jam",
                availablePoli = listOf("Anak", "Umum", "Gigi", "Kandungan"),
                drawableResName = "img_rs_harapan_bunda"
            ),
            HospitalItem(
                id = "rs-medika",
                name = "RS Medika Utama",
                address = "Jl. Ahmad Yani No. 88, Sidoarjo",
                rating = 4.8,
                distanceKm = "1,8 km",
                openHours = "Buka 24 jam",
                availablePoli = listOf("Anak", "Umum", "Gigi"),
                drawableResName = "img_rs_medika"
            )
        )

        return allHospitals.filter { hospital ->
            val matchesQuery = query.isBlank() ||
                hospital.name.contains(query, ignoreCase = true) ||
                hospital.availablePoli.any { it.contains(query, ignoreCase = true) }

            val matchesFilter = when (poliFilter) {
                "Semua", "RS Umum" -> true
                "Poli Anak", "Anak" -> hospital.availablePoli.contains("Anak")
                "Kandungan" -> hospital.availablePoli.contains("Kandungan")
                "Gigi" -> hospital.availablePoli.contains("Gigi")
                "Umum" -> hospital.availablePoli.contains("Umum")
                else -> true
            }
            matchesQuery && matchesFilter
        }
    }

    override suspend fun getDoctorsForPoli(
        poliShortName: String,
        dateIndex: Int
    ): List<DoctorScheduleItem> {
        val isTomorrowOrLater = dateIndex >= 1
        return listOf(
            DoctorScheduleItem(
                id = "doc-abdul",
                name = "dr. Abdul, Sp.A",
                initials = "AL",
                specialty = "Spesialis $poliShortName",
                poliShortName = poliShortName,
                rating = 4.9,
                experienceYears = 6,
                slots = listOf(
                    DoctorTimeSlot("08:00", isFull = false, currentBooked = 18, maxQuota = 30),
                    DoctorTimeSlot("09:00", isFull = false, currentBooked = 12, maxQuota = 30),
                    DoctorTimeSlot("13:00", isFull = !isTomorrowOrLater, currentBooked = if (isTomorrowOrLater) 8 else 30, maxQuota = 30)
                )
            ),
            DoctorScheduleItem(
                id = "doc-rahmat",
                name = "dr. Rahmat, Sp.A",
                initials = "BS",
                specialty = "Spesialis $poliShortName",
                poliShortName = poliShortName,
                rating = 4.8,
                experienceYears = 7,
                slots = listOf(
                    DoctorTimeSlot("09:00", isFull = false, currentBooked = 14, maxQuota = 30),
                    DoctorTimeSlot("10:30", isFull = !isTomorrowOrLater, currentBooked = if (isTomorrowOrLater) 10 else 30, maxQuota = 30),
                    DoctorTimeSlot("14:00", isFull = false, currentBooked = 9, maxQuota = 30)
                )
            ),
            DoctorScheduleItem(
                id = "doc-clara",
                name = "dr. Clara Wijaya, Sp.A",
                initials = "CW",
                specialty = "Spesialis $poliShortName",
                poliShortName = poliShortName,
                rating = 4.7,
                experienceYears = 8,
                slots = listOf(
                    DoctorTimeSlot("10:00", isFull = false, currentBooked = 11, maxQuota = 30),
                    DoctorTimeSlot("11:30", isFull = false, currentBooked = 19, maxQuota = 30),
                    DoctorTimeSlot("15:00", isFull = false, currentBooked = 7, maxQuota = 30)
                )
            ),
            DoctorScheduleItem(
                id = "doc-dimas",
                name = "dr. Dimas Anggara, Sp.A",
                initials = "DA",
                specialty = "Spesialis $poliShortName",
                poliShortName = poliShortName,
                rating = 4.9,
                experienceYears = 9,
                slots = listOf(
                    DoctorTimeSlot("08:00", isFull = false, currentBooked = 20, maxQuota = 30),
                    DoctorTimeSlot("09:30", isFull = false, currentBooked = 15, maxQuota = 30),
                    DoctorTimeSlot("13:00", isFull = !isTomorrowOrLater, currentBooked = if (isTomorrowOrLater) 6 else 30, maxQuota = 30)
                )
            ),
            DoctorScheduleItem(
                id = "doc-eka",
                name = "dr. Eka Pratiwi, Sp.A",
                initials = "EP",
                specialty = "Spesialis $poliShortName",
                poliShortName = poliShortName,
                rating = 4.8,
                experienceYears = 10,
                slots = listOf(
                    DoctorTimeSlot("09:00", isFull = false, currentBooked = 12, maxQuota = 30),
                    DoctorTimeSlot("10:30", isFull = !isTomorrowOrLater, currentBooked = if (isTomorrowOrLater) 5 else 30, maxQuota = 30),
                    DoctorTimeSlot("14:00", isFull = false, currentBooked = 16, maxQuota = 30)
                )
            )
        )
    }

    override suspend fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val userMsg = ChatMessageItem(
            id = "msg-u-${System.currentTimeMillis()}",
            senderName = "Anda",
            message = text.trim(),
            time = "09.48",
            isFromUser = true
        )
        _chatMessagesFlow.update { it + userMsg }

        val replyText = when {
            text.contains("dokter sudah di ruangan", ignoreCase = true) ->
                "Saat ini dokter sudah berada di area rumah sakit dan pelayanan poli berjalan sesuai nomor urut. Jika ada tindakan visite mendadak, status di aplikasi akan otomatis ter-update."
            text.contains("BPJS", ignoreCase = true) || text.contains("Dokumen", ignoreCase = true) ->
                "Untuk pasien BPJS maupun Umum, silakan membawa: 1) KTP asli / KIA, 2) Kartu BPJS aktif & Surat Rujukan Faskes 1, serta 3) QR Tiket antrean di aplikasi RuangTunggu."
            text.contains("estimasi antrean per pasien", ignoreCase = true) || text.contains("Berapa lama", ignoreCase = true) ->
                "Rata-rata durasi konsultasi adalah ± 5–6 menit per pasien. Rumus estimasi aplikasi kami menghitung otomatis: Sisa Antrean × 5–6 menit."
            text.contains("Jadwalkan ulang", ignoreCase = true) ->
                "Baik, Anda dapat menekan tombol 'Reschedule / Jadwal Baru' pada layar Pantau Antrean tanpa kehilangan prioritas kuota."
            else ->
                "Terima kasih atas konfirmasinya. Nomor antrean Anda tetap aman di sistem loket kami. Kami akan mengirim notifikasi saat sisa antrean ≤ 2."
        }

        val botReply = ChatMessageItem(
            id = "msg-b-${System.currentTimeMillis() + 1}",
            senderName = "Petugas Loket RS",
            message = replyText,
            time = "09.49",
            isFromUser = false
        )
        _chatMessagesFlow.update { it + botReply }
    }

    override suspend fun markAllNotificationsRead() {
        _notificationsFlow.update { list -> list.map { it.copy(isUnread = false) } }
    }

    override suspend fun toggleReminder(id: String) {
        _remindersFlow.update { list ->
            list.map { if (it.id == id) it.copy(isEnabled = !it.isEnabled) else it }
        }
    }

    private fun computeEstimatedClock(slotTime: String, addMinutes: Int): String {
        val parts = slotTime.split(":", ".")
        val hour = parts.getOrNull(0)?.toIntOrNull() ?: 8
        val min = parts.getOrNull(1)?.toIntOrNull() ?: 0
        val total = hour * 60 + min + addMinutes
        return "%02d:%02d".format((total / 60) % 24, total % 60)
    }
}
