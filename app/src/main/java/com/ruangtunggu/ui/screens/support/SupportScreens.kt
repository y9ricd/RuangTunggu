package com.ruangtunggu.ui.screens.support

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.ruangtunggu.domain.model.ActiveQueue
import com.ruangtunggu.domain.model.ChatMessageItem
import com.ruangtunggu.domain.model.NotificationItem
import com.ruangtunggu.domain.model.PatientProfile
import com.ruangtunggu.domain.model.ReminderToggleItem
import com.ruangtunggu.domain.repository.AuthRepository
import com.ruangtunggu.domain.repository.QueueRepository
import com.ruangtunggu.ui.components.RTCurvedHeader
import com.ruangtunggu.ui.components.RTEmptyStateView
import com.ruangtunggu.ui.components.RTFloatingCard
import com.ruangtunggu.ui.components.RTHeaderBackButton
import com.ruangtunggu.ui.components.RTPrimaryButton
import com.ruangtunggu.ui.components.RTTextField
import com.ruangtunggu.ui.components.springClickable
import com.ruangtunggu.ui.theme.RTColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SupportUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val activeQueue: ActiveQueue? = null,
    val chatMessages: List<ChatMessageItem> = emptyList(),
    val notifications: List<NotificationItem> = emptyList(),
    val reminders: List<ReminderToggleItem> = emptyList(),
    val profile: PatientProfile = PatientProfile(),
    val selectedNotifFilter: String = "Semua",
    val generatedOtpCode: String? = null,
    val isPasswordResetDone: Boolean = false
)

@HiltViewModel
class SupportViewModel @Inject constructor(
    private val queueRepository: QueueRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SupportUiState())
    val uiState: StateFlow<SupportUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            queueRepository.activeQueueFlow.collect { q ->
                _uiState.update { it.copy(activeQueue = q) }
            }
        }
        viewModelScope.launch {
            queueRepository.chatMessagesFlow.collect { msgs ->
                _uiState.update { it.copy(chatMessages = msgs) }
            }
        }
        viewModelScope.launch {
            queueRepository.notificationsFlow.collect { notifs ->
                _uiState.update { it.copy(notifications = notifs) }
            }
        }
        viewModelScope.launch {
            queueRepository.remindersFlow.collect { rems ->
                _uiState.update { it.copy(reminders = rems) }
            }
        }
        viewModelScope.launch {
            authRepository.currentProfileFlow.collect { prof ->
                _uiState.update { it.copy(profile = prof) }
            }
        }
    }

    fun sendChatMessage(text: String) {
        viewModelScope.launch {
            queueRepository.sendChatMessage(text)
        }
    }

    fun setNotifFilter(filter: String) {
        _uiState.update { it.copy(selectedNotifFilter = filter) }
    }

    fun markAllNotificationsRead() {
        viewModelScope.launch {
            queueRepository.markAllNotificationsRead()
        }
    }

    fun toggleReminder(id: String) {
        viewModelScope.launch {
            queueRepository.toggleReminder(id)
        }
    }

    fun submitReview(
        stars: Int,
        tags: List<String>,
        comment: String,
        isAnonymous: Boolean,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            queueRepository.submitConsultationReview(stars, tags, comment, isAnonymous)
            onDone()
        }
    }

    fun confirmReschedule(onDone: () -> Unit) {
        viewModelScope.launch {
            queueRepository.rescheduleActiveQueue("Jum, 23 Okt", "09:00")
            onDone()
        }
    }

    fun saveProfileChanges(
        fullName: String,
        nik: String,
        birthDate: String,
        gender: String,
        phone: String,
        onSaved: () -> Unit
    ) {
        viewModelScope.launch {
            authRepository.updateProfile(fullName, nik, birthDate, gender, phone)
            onSaved()
        }
    }

    fun sendForgotPasswordOtp(nikOrEmail: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = authRepository.requestPasswordResetOtp(nikOrEmail)
            result.fold(
                onSuccess = { otp ->
                    queueRepository.addSystemAndInAppNotification(
                        title = "Kode OTP Reset Kata Sandi: $otp",
                        body = "Kode verifikasi pemulihan akun RuangTunggu untuk $nikOrEmail adalah $otp.",
                        category = "INFO"
                    )
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            generatedOtpCode = otp,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { err ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = err.message ?: "Gagal mengirim kode OTP"
                        )
                    }
                }
            )
        }
    }

    fun verifyOtpAndResetPassword(
        nikOrEmail: String,
        otpInput: String,
        newPassword: String
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = authRepository.confirmPasswordReset(nikOrEmail, otpInput, newPassword)
            result.fold(
                onSuccess = {
                    queueRepository.addSystemAndInAppNotification(
                        title = "Kata Sandi Berhasil Diubah ✓",
                        body = "Kata sandi akun RuangTunggu Anda telah diperbarui dengan aman.",
                        category = "INFO"
                    )
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isPasswordResetDone = true,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { err ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = err.message ?: "Kode OTP atau kata sandi tidak sesuai"
                        )
                    }
                }
            )
        }
    }
}

@Suppress("FunctionName")
@Composable
fun ChatPetugasScreen(
    onBackClick: () -> Unit,
    viewModel: SupportViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var messageDraft by remember { mutableStateOf("") }

    val faqQuickChips = listOf(
        "💊 Apakah dokter sudah di ruangan?",
        "📄 Dokumen BPJS apa saja yang harus dibawa?",
        "⏱️ Berapa lama estimasi antrean per pasien?"
    )
    val statusReplyChips = listOf(
        "Saya menunggu",
        "Jadwalkan ulang",
        "Batalkan"
    )

    Scaffold(
        containerColor = RTColors.ScreenBackground,
        bottomBar = {
            Surface(color = RTColors.PureWhite, shadowElevation = 16.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(top = 10.dp, bottom = 12.dp)
                ) {
                    Text(
                        text = "Pertanyaan cepat (Klik untuk langsung kirim):",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTColors.SlateBodyText,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        faqQuickChips.forEach { chipText ->
                            Box(
                                modifier = Modifier
                                    .height(34.dp)
                                    .springClickable {
                                        viewModel.sendChatMessage(chipText)
                                    }
                                    .clip(RoundedCornerShape(17.dp))
                                    .background(RTColors.QuickActionUniformBg)
                                    .border(1.dp, RTColors.MintSurfaceActive, RoundedCornerShape(17.dp))
                                    .padding(horizontal = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = chipText,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RTColors.BrandDarkTeal
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        statusReplyChips.forEach { reply ->
                            Box(
                                modifier = Modifier
                                    .height(32.dp)
                                    .springClickable {
                                        viewModel.sendChatMessage(reply)
                                    }
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(RTColors.PureWhite)
                                    .border(1.dp, RTColors.InputBorder, RoundedCornerShape(16.dp))
                                    .padding(horizontal = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = reply,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RTColors.NavyHeading
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = messageDraft,
                            onValueChange = { messageDraft = it },
                            singleLine = true,
                            textStyle = TextStyle(fontSize = 14.sp, color = RTColors.NavyHeading),
                            cursorBrush = SolidColor(RTColors.BrandPrimaryTeal),
                            modifier = Modifier.weight(1f),
                            decorationBox = { innerTextField ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(46.dp)
                                        .clip(RoundedCornerShape(23.dp))
                                        .background(RTColors.InputBackground)
                                        .border(1.dp, RTColors.InputBorder, RoundedCornerShape(23.dp))
                                        .padding(horizontal = 18.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    if (messageDraft.isEmpty()) {
                                        Text(
                                            text = "Tulis pesan...",
                                            fontSize = 13.5.sp,
                                            color = RTColors.MutedSlate
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .springClickable {
                                    if (messageDraft.isNotBlank()) {
                                        viewModel.sendChatMessage(messageDraft)
                                        messageDraft = ""
                                    }
                                }
                                .clip(CircleShape)
                                .background(RTColors.BrandPrimaryTeal),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Kirim",
                                tint = RTColors.PureWhite,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            RTCurvedHeader(height = 148.dp, bottomCornerRadius = 28.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(top = 16.dp, start = 20.dp, end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RTHeaderBackButton(onClick = onBackClick)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Chat Petugas",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.PureWhite
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(RTColors.MintSurfaceActive)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Online · Loket Pendaftaran",
                                fontSize = 12.5.sp,
                                color = RTColors.PureWhite.copy(alpha = 0.92f)
                            )
                        }
                    }
                }
            }

            val activeNumber = uiState.activeQueue?.userQueueNumber ?: "Belum ada antrean"
            val activePoli = uiState.activeQueue?.poliName ?: "Layanan Informasi RS"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(RTColors.WarningAmberBg)
                    .border(1.dp, RTColors.WarningAmberBorder, RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(RTColors.WarningAmberText),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PriorityHigh,
                        contentDescription = null,
                        tint = RTColors.PureWhite,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Status: $activeNumber · $activePoli",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.WarningAmberText
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                uiState.chatMessages.forEach { msg ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = if (msg.isFromUser) Alignment.CenterEnd else Alignment.CenterStart
                    ) {
                        Column(
                            modifier = Modifier
                                .widthIn(max = 290.dp)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 18.dp,
                                        topEnd = 18.dp,
                                        bottomStart = if (msg.isFromUser) 18.dp else 4.dp,
                                        bottomEnd = if (msg.isFromUser) 4.dp else 18.dp
                                    )
                                )
                                .background(
                                    if (msg.isFromUser) RTColors.BrandPrimaryTeal else RTColors.PureWhite
                                )
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = msg.message,
                                fontSize = 13.5.sp,
                                lineHeight = 20.sp,
                                color = if (msg.isFromUser) RTColors.PureWhite else RTColors.NavyHeading
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = msg.time,
                                fontSize = 10.5.sp,
                                color = if (msg.isFromUser) {
                                    RTColors.PureWhite.copy(alpha = 0.8f)
                                } else {
                                    RTColors.MutedSlate
                                },
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Suppress("FunctionName")
@Composable
fun BeriPenilaianScreen(
    onBackClick: () -> Unit,
    onReviewSubmitted: () -> Unit,
    viewModel: SupportViewModel = hiltViewModel()
) {
    var selectedStars by remember { mutableIntStateOf(5) }
    val allTags = listOf("Ramah", "Tepat waktu", "Penjelasan jelas", "Ruang bersih", "Antrean tertib")
    val selectedTags = remember { mutableStateListOf("Ramah", "Tepat waktu", "Antrean tertib") }
    var reviewText by remember { mutableStateOf("") }
    var isAnonymous by remember { mutableStateOf(false) }

    val ratingLabel = when (selectedStars) {
        1 -> "Kurang Memuaskan"
        2 -> "Cukup"
        3 -> "Baik"
        4 -> "Sangat Baik"
        else -> "Luar Biasa!"
    }

    Scaffold(
        containerColor = RTColors.ScreenBackground,
        bottomBar = {
            Surface(color = RTColors.PureWhite, shadowElevation = 16.dp) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    RTPrimaryButton(
                        text = "Kirim penilaian",
                        onClick = {
                            viewModel.submitReview(
                                stars = selectedStars,
                                tags = selectedTags.toList(),
                                comment = reviewText,
                                isAnonymous = isAnonymous,
                                onDone = onReviewSubmitted
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            RTCurvedHeader(height = 168.dp, bottomCornerRadius = 32.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(top = 18.dp, start = 20.dp, end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RTHeaderBackButton(onClick = onBackClick)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Beri Penilaian",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.PureWhite
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Poli Anak · RS Sehat Sidoarjo",
                            fontSize = 12.5.sp,
                            color = RTColors.PureWhite.copy(alpha = 0.92f)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-36).dp)
                    .padding(horizontal = 20.dp)
            ) {
                RTFloatingCard(
                    cornerRadius = 22.dp,
                    contentPadding = PaddingValues(22.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Penilaian Layanan",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.NavyHeading
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Kunjungan Kam, 22 Okt 2026 · dr. Abdul, Sp.A",
                            fontSize = 12.5.sp,
                            color = RTColors.SlateBodyText
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            for (star in 1..5) {
                                val filled = star <= selectedStars
                                Icon(
                                    imageVector = if (filled) Icons.Default.Star else Icons.Outlined.StarOutline,
                                    contentDescription = "Bintang $star",
                                    tint = if (filled) RTColors.StarGold else RTColors.InputBorder,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .springClickable { selectedStars = star }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = ratingLabel,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.WarningAmberText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Apa yang Anda sukai?",
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.NavyHeading
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        allTags.take(3).forEach { tag ->
                            FeedbackTagChip(
                                tag = tag,
                                isSelected = selectedTags.contains(tag),
                                onClick = {
                                    if (selectedTags.contains(tag)) selectedTags.remove(tag) else selectedTags.add(tag)
                                }
                            )
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        allTags.drop(3).forEach { tag ->
                            FeedbackTagChip(
                                tag = tag,
                                isSelected = selectedTags.contains(tag),
                                onClick = {
                                    if (selectedTags.contains(tag)) selectedTags.remove(tag) else selectedTags.add(tag)
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Ulasan (opsional)",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.SlateBodyText
                )

                Spacer(modifier = Modifier.height(8.dp))

                BasicTextField(
                    value = reviewText,
                    onValueChange = { if (it.length <= 200) reviewText = it },
                    textStyle = TextStyle(fontSize = 14.sp, color = RTColors.NavyHeading),
                    cursorBrush = SolidColor(RTColors.BrandPrimaryTeal),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(RTColors.PureWhite)
                                .border(1.dp, RTColors.InputBorder, RoundedCornerShape(16.dp))
                                .padding(14.dp)
                        ) {
                            if (reviewText.isEmpty()) {
                                Text(
                                    text = "Tulis pengalaman Anda...",
                                    fontSize = 13.5.sp,
                                    color = RTColors.MutedSlate
                                )
                            }
                            innerTextField()
                            Text(
                                text = "${reviewText.length}/200",
                                fontSize = 11.sp,
                                color = RTColors.MutedSlate,
                                modifier = Modifier.align(Alignment.BottomEnd)
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = RTColors.PureWhite
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Kirim sebagai anonim",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.NavyHeading
                        )
                        Switch(
                            checked = isAnonymous,
                            onCheckedChange = { isAnonymous = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = RTColors.PureWhite,
                                checkedTrackColor = RTColors.BrandPrimaryTeal
                            )
                        )
                    }
                }
            }
        }
    }
}

@Suppress("FunctionName")
@Composable
private fun FeedbackTagChip(
    tag: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(36.dp)
            .springClickable(onClick = onClick)
            .clip(RoundedCornerShape(18.dp))
            .background(if (isSelected) RTColors.BrandPrimaryTeal else RTColors.PureWhite)
            .border(
                1.dp,
                if (isSelected) RTColors.BrandPrimaryTeal else RTColors.InputBorder,
                RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tag,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) RTColors.PureWhite else RTColors.NavyHeading
        )
    }
}

@Suppress("FunctionName")
@Composable
fun NotifikasiScreen(
    onBackClick: () -> Unit,
    viewModel: SupportViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val unreadCount = uiState.notifications.count { it.isUnread }

    val filteredList = when (uiState.selectedNotifFilter) {
        "Antrean" -> uiState.notifications.filter { it.category == "ANTREAN" }
        "Info" -> uiState.notifications.filter { it.category == "INFO" }
        else -> uiState.notifications
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RTColors.ScreenBackground)
    ) {
        RTCurvedHeader(height = 148.dp, bottomCornerRadius = 28.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 18.dp, start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RTHeaderBackButton(onClick = onBackClick)
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Notifikasi",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTColors.PureWhite
                    )
                    Text(
                        text = "$unreadCount belum dibaca",
                        fontSize = 12.5.sp,
                        color = RTColors.PureWhite.copy(alpha = 0.9f)
                    )
                }
                Text(
                    text = "Tandai dibaca",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.PureWhite,
                    modifier = Modifier
                        .springClickable { viewModel.markAllNotificationsRead() }
                        .padding(vertical = 6.dp, horizontal = 8.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Semua", "Antrean", "Info").forEach { tab ->
                    val selected = tab == uiState.selectedNotifFilter
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .springClickable { viewModel.setNotifFilter(tab) }
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (selected) RTColors.BrandPrimaryTeal else RTColors.PureWhite)
                            .border(
                                1.dp,
                                if (selected) RTColors.BrandPrimaryTeal else RTColors.InputBorder,
                                RoundedCornerShape(18.dp)
                            )
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selected) RTColors.PureWhite else RTColors.NavyHeading
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredList.isEmpty()) {
                RTEmptyStateView(
                    icon = Icons.Outlined.NotificationsNone,
                    title = "Belum Ada Notifikasi",
                    description = "Notifikasi terkait panggilan nomor antrean dan pembaruan jadwal dokter akan muncul di sini."
                )
            } else {
                filteredList.forEach { item ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .springClickable { viewModel.markAllNotificationsRead() },
                        shape = RoundedCornerShape(18.dp),
                        color = RTColors.PureWhite,
                        shadowElevation = 3.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(RTColors.QuickActionUniformBg),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (item.title.contains("dijeda")) {
                                        Icons.Default.Pause
                                    } else if (item.category == "ANTREAN") {
                                        Icons.Default.Notifications
                                    } else {
                                        Icons.Outlined.FavoriteBorder
                                    },
                                    contentDescription = null,
                                    tint = RTColors.BrandPrimaryTeal,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.title,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RTColors.NavyHeading,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = item.timeAgo,
                                        fontSize = 11.sp,
                                        color = RTColors.MutedSlate
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.body,
                                    fontSize = 12.5.sp,
                                    lineHeight = 18.sp,
                                    color = RTColors.SlateBodyText
                                )
                            }

                            if (item.isUnread) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .size(9.dp)
                                        .clip(CircleShape)
                                        .background(RTColors.BrandPrimaryTeal)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Suppress("FunctionName")
@Composable
fun AturPengingatScreen(
    onBackClick: () -> Unit,
    viewModel: SupportViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = RTColors.ScreenBackground,
        bottomBar = {
            Surface(color = RTColors.PureWhite, shadowElevation = 16.dp) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    RTPrimaryButton(
                        text = "Simpan pengingat",
                        onClick = onBackClick
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            RTCurvedHeader(height = 168.dp, bottomCornerRadius = 32.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(top = 18.dp, start = 20.dp, end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RTHeaderBackButton(onClick = onBackClick)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Atur Pengingat",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.PureWhite
                        )
                        Text(
                            text = uiState.activeQueue?.poliName ?: "Poli Anak · Kam, 22 Okt",
                            fontSize = 12.5.sp,
                            color = RTColors.PureWhite.copy(alpha = 0.92f)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-36).dp)
                    .padding(horizontal = 20.dp)
            ) {
                RTFloatingCard(cornerRadius = 20.dp, contentPadding = PaddingValues(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(RTColors.MintSurfaceActive),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CalendarToday,
                                contentDescription = null,
                                tint = RTColors.BrandPrimaryTeal,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = uiState.activeQueue?.poliName ?: "Pemeriksaan Poli Anak",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTColors.NavyHeading
                            )
                            Text(
                                text = "${uiState.activeQueue?.scheduleDateTime ?: "Kam, 22 Okt · 08:00"} · ${uiState.activeQueue?.hospitalName ?: "RS Sehat Sidoarjo"}",
                                fontSize = 12.sp,
                                color = RTColors.SlateBodyText
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Pengingat",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.SlateBodyText
                )

                Spacer(modifier = Modifier.height(10.dp))

                uiState.reminders.forEach { item ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        color = RTColors.PureWhite,
                        shadowElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = item.title,
                                    fontSize = 14.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RTColors.NavyHeading
                                )
                                Text(
                                    text = item.scheduleLabel,
                                    fontSize = 12.sp,
                                    color = RTColors.SlateBodyText
                                )
                            }
                            Switch(
                                checked = item.isEnabled,
                                onCheckedChange = { viewModel.toggleReminder(item.id) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = RTColors.PureWhite,
                                    checkedTrackColor = RTColors.BrandPrimaryTeal
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(RTColors.QuickActionUniformBg)
                        .border(1.dp, RTColors.BrandPrimaryTeal, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = RTColors.BrandPrimaryTeal,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Aktifkan izin notifikasi agar pengingat tampil tepat waktu",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTColors.BrandPrimaryTeal
                    )
                }
            }
        }
    }
}

@Suppress("FunctionName")
@Composable
fun RescheduleJadwalScreen(
    onBackClick: () -> Unit,
    onRescheduleConfirmed: () -> Unit,
    viewModel: SupportViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val queue = uiState.activeQueue ?: ActiveQueue()

    Scaffold(
        containerColor = RTColors.ScreenBackground,
        bottomBar = {
            Surface(color = RTColors.PureWhite, shadowElevation = 16.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onBackClick,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, RTColors.BrandPrimaryTeal),
                        modifier = Modifier
                            .weight(0.45f)
                            .height(50.dp)
                    ) {
                        Text(
                            text = "Jadwal lain",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.BrandPrimaryTeal
                        )
                    }

                    Box(modifier = Modifier.weight(0.55f)) {
                        RTPrimaryButton(
                            text = "Setuju jadwal baru",
                            onClick = { viewModel.confirmReschedule(onRescheduleConfirmed) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            RTCurvedHeader(height = 168.dp, bottomCornerRadius = 32.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(top = 18.dp, start = 20.dp, end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RTHeaderBackButton(onClick = onBackClick)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Jadwal Dokter Berubah",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.PureWhite
                        )
                        Text(
                            text = "Mohon konfirmasi pilihan Anda",
                            fontSize = 12.5.sp,
                            color = RTColors.PureWhite.copy(alpha = 0.92f)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-36).dp)
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(RTColors.WarningAmberBg)
                        .border(1.dp, RTColors.WarningAmberBorder, RoundedCornerShape(20.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(RTColors.WarningAmberText),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = RTColors.PureWhite,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Dokter berhalangan",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.WarningAmberText
                        )
                        Text(
                            text = "Jadwal Anda dijadwalkan ulang oleh RS.\nTidak ada biaya tambahan.",
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            color = RTColors.WarningAmberText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                RTFloatingCard(cornerRadius = 22.dp, contentPadding = PaddingValues(18.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(RTColors.QuickActionUniformBg)
                            .border(1.dp, RTColors.BrandPrimaryTeal, RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "JADWAL SEMULA",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.SlateBodyText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = queue.doctorName,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTColors.SlateBodyText
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(RTColors.InputBorder)
                                    .padding(horizontal = 10.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "No. ${queue.userQueueNumber}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RTColors.NavyHeading
                                )
                            }
                        }
                        Text(
                            text = queue.scheduleDateTime,
                            fontSize = 12.5.sp,
                            color = RTColors.SlateBodyText
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .align(Alignment.CenterHorizontally)
                            .clip(CircleShape)
                            .background(RTColors.MintSurfaceActive),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = null,
                            tint = RTColors.BrandPrimaryTeal,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(RTColors.QuickActionUniformBg)
                            .border(1.5.dp, RTColors.BrandPrimaryTeal, RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "JADWAL BARU",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.BrandPrimaryTeal
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = queue.doctorName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.NavyHeading
                        )
                        Text(
                            text = "Jum, 23 Okt 2026 · 09:00",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.BrandPrimaryTeal
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(RTColors.BrandPrimaryTeal)
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "No. antrean baru A-007",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTColors.PureWhite
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${queue.poliName} · ${queue.hospitalName}",
                            fontSize = 12.sp,
                            color = RTColors.SlateBodyText
                        )
                    }
                }
            }
        }
    }
}

@Suppress("FunctionName")
@Composable
fun UbahProfilScreen(
    onBackClick: () -> Unit,
    viewModel: SupportViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var fullName by remember(uiState.profile.fullName) { mutableStateOf(uiState.profile.fullName) }
    var nik by remember(uiState.profile.nik) { mutableStateOf(uiState.profile.nik) }
    var birthDate by remember(uiState.profile.birthPlaceDate) { mutableStateOf(uiState.profile.birthPlaceDate) }
    var gender by remember(uiState.profile.gender) { mutableStateOf(uiState.profile.gender) }
    var phone by remember(uiState.profile.phone) { mutableStateOf(uiState.profile.phone) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RTColors.ScreenBackground)
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        RTCurvedHeader(height = 168.dp, bottomCornerRadius = 32.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 18.dp, start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RTHeaderBackButton(onClick = onBackClick)
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Ubah Profil",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTColors.PureWhite
                    )
                    Text(
                        text = "Perbarui data diri Anda",
                        fontSize = 12.5.sp,
                        color = RTColors.PureWhite.copy(alpha = 0.92f)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-36).dp)
                .padding(horizontal = 20.dp)
        ) {
            RTFloatingCard(cornerRadius = 22.dp, contentPadding = PaddingValues(20.dp)) {
                RTTextField(
                    label = "Nama lengkap",
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = "Nama Pasien",
                    leadingIcon = Icons.Outlined.PersonOutline
                )
                Spacer(modifier = Modifier.height(12.dp))
                RTTextField(
                    label = "NIK",
                    value = nik,
                    onValueChange = { nik = it },
                    placeholder = "16 digit NIK",
                    leadingIcon = Icons.Outlined.Badge,
                    maxLength = 16,
                    digitsOnly = true
                )
                Spacer(modifier = Modifier.height(12.dp))
                RTTextField(
                    label = "Tanggal lahir",
                    value = birthDate,
                    onValueChange = { birthDate = it },
                    placeholder = "DD/MM/YYYY",
                    leadingIcon = Icons.Outlined.CalendarToday
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Jenis kelamin",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.NavyHeading
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf("Laki-laki", "Perempuan").forEach { option ->
                        val active = gender == option
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .springClickable { gender = option }
                                .clip(RoundedCornerShape(13.dp))
                                .background(if (active) RTColors.QuickActionUniformBg else RTColors.InputBackground)
                                .border(
                                    1.2.dp,
                                    if (active) RTColors.BrandPrimaryTeal else RTColors.InputBorder,
                                    RoundedCornerShape(13.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (active) "● $option" else "○ $option",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (active) RTColors.BrandPrimaryTeal else RTColors.SlateBodyText
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                RTTextField(
                    label = "No. HP",
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = "08xxxxxxxxxx",
                    leadingIcon = Icons.Outlined.PhoneAndroid
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            RTPrimaryButton(
                text = "Simpan perubahan",
                onClick = {
                    viewModel.saveProfileChanges(fullName, nik, birthDate, gender, phone, onBackClick)
                }
            )
        }
    }
}

/**
 * Complete Interactive Lupa Kata Sandi Screen matching Lupa sandi.png,
 * verifikasi kode lupa kata sandi.png, and Kata sandi berhasil dirubah.png!
 * - Sends a REAL Android System Notification containing the 6-digit OTP code
 * - Displays an interactive OTP banner with 1-click "Isi Otomatis Kode OTP"
 * - Updates the user's password in Local Database so they can immediately log in with the new password
 */
@Suppress("FunctionName")
@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit,
    viewModel: SupportViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var emailOrNikInput by remember { mutableStateOf("") }
    var otpInput by remember { mutableStateOf("") }
    var newPasswordInput by remember { mutableStateOf("") }

    // Auto-fill OTP convenience when user taps banner
    LaunchedEffect(uiState.generatedOtpCode) {
        if (uiState.generatedOtpCode != null && otpInput.isEmpty()) {
            // Keep otpInput ready or let user tap banner to auto-fill
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RTColors.ScreenBackground)
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        RTCurvedHeader(height = 148.dp, bottomCornerRadius = 28.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 18.dp, start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RTHeaderBackButton(onClick = onBackClick)
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Lupa Kata Sandi",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTColors.PureWhite
                    )
                    Text(
                        text = "Pulihkan akses akun Anda",
                        fontSize = 12.5.sp,
                        color = RTColors.PureWhite.copy(alpha = 0.92f)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(22.dp))

            // Top Illustration Circle
            Box(
                modifier = Modifier
                    .size(132.dp)
                    .clip(CircleShape)
                    .background(RTColors.QuickActionUniformBg),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(92.dp)
                        .clip(CircleShape)
                        .background(RTColors.MintSurfaceActive),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (uiState.isPasswordResetDone) {
                            Icons.Default.Check
                        } else {
                            Icons.Outlined.Lock
                        },
                        contentDescription = null,
                        tint = RTColors.BrandPrimaryTeal,
                        modifier = Modifier.size(44.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (uiState.isPasswordResetDone) {
                // Step 3: Password Successfully Changed (Kata sandi berhasil dirubah.png)
                Text(
                    text = "Kata sandi berhasil diubah!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.NavyHeading
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Kata sandi baru untuk akun $emailOrNikInput telah disimpan di database. Silakan masuk kembali.",
                    fontSize = 13.5.sp,
                    lineHeight = 20.sp,
                    color = RTColors.SlateBodyText,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                RTPrimaryButton(
                    text = "Masuk sekarang",
                    onClick = onBackClick
                )
            } else {
                Text(
                    text = if (uiState.generatedOtpCode == null) {
                        "Atur ulang kata sandi"
                    } else {
                        "Verifikasi Kode OTP"
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.NavyHeading
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (uiState.generatedOtpCode == null) {
                        "Masukkan NIK atau email yang terdaftar.\nKami akan mengirim notifikasi kode verifikasi."
                    } else {
                        "Kode OTP telah dikirim ke bar notifikasi ponsel Anda.\nMasukkan kode OTP dan kata sandi baru."
                    },
                    fontSize = 13.5.sp,
                    lineHeight = 20.sp,
                    color = RTColors.SlateBodyText,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Interactive Push Notification Banner when OTP is generated
                val currentOtp = uiState.generatedOtpCode
                if (currentOtp != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .springClickable { otpInput = currentOtp }
                            .clip(RoundedCornerShape(16.dp))
                            .background(RTColors.QuickActionUniformBg)
                            .border(1.5.dp, RTColors.BrandPrimaryTeal, RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(RTColors.BrandPrimaryTeal),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = RTColors.PureWhite,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Notifikasi Terkirim · Kode OTP: $currentOtp",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = RTColors.BrandDarkTeal
                            )
                            Text(
                                text = "Ketuk banner ini untuk mengisi kode OTP otomatis",
                                fontSize = 11.5.sp,
                                color = RTColors.BrandPrimaryTeal
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                RTFloatingCard(cornerRadius = 22.dp, contentPadding = PaddingValues(20.dp)) {
                    RTTextField(
                        label = "NIK / Email Terdaftar",
                        value = emailOrNikInput,
                        onValueChange = { emailOrNikInput = it },
                        placeholder = "Masukkan NIK atau email terdaftar",
                        leadingIcon = Icons.Outlined.Badge,
                        isValidCheck = emailOrNikInput.trim().length >= 5
                    )

                    if (currentOtp != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        RTTextField(
                            label = "Kode Verifikasi OTP (6 Digit)",
                            value = otpInput,
                            onValueChange = { otpInput = it },
                            placeholder = "Contoh: $currentOtp",
                            leadingIcon = Icons.Outlined.VerifiedUser,
                            keyboardType = KeyboardType.Number,
                            maxLength = 6,
                            digitsOnly = true,
                            isValidCheck = otpInput == currentOtp
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        RTTextField(
                            label = "Kata Sandi Baru",
                            value = newPasswordInput,
                            onValueChange = { newPasswordInput = it },
                            placeholder = "Minimal 8 karakter",
                            leadingIcon = Icons.Outlined.Lock,
                            isPassword = true,
                            keyboardType = KeyboardType.Password
                        )
                    }

                    if (uiState.errorMessage != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = uiState.errorMessage!!,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.ErrorRed
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    if (currentOtp == null) {
                        RTPrimaryButton(
                            text = "Kirim kode verifikasi",
                            isLoading = uiState.isLoading,
                            onClick = { viewModel.sendForgotPasswordOtp(emailOrNikInput) }
                        )
                    } else {
                        RTPrimaryButton(
                            text = "Simpan kata sandi baru",
                            isLoading = uiState.isLoading,
                            onClick = {
                                viewModel.verifyOtpAndResetPassword(
                                    nikOrEmail = emailOrNikInput,
                                    otpInput = otpInput,
                                    newPassword = newPasswordInput
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Kirim ulang kode OTP",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.BrandPrimaryTeal,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .springClickable { viewModel.sendForgotPasswordOtp(emailOrNikInput) }
                                .padding(4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Kembali ke halaman masuk",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.BrandPrimaryTeal,
                    modifier = Modifier
                        .springClickable(onClick = onBackClick)
                        .padding(vertical = 6.dp, horizontal = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
