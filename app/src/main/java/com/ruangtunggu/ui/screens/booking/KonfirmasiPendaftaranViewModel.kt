package com.ruangtunggu.ui.screens.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruangtunggu.domain.model.ActiveQueue
import com.ruangtunggu.domain.model.DoctorScheduleItem
import com.ruangtunggu.domain.model.DoctorTimeSlot
import com.ruangtunggu.domain.model.HospitalItem
import com.ruangtunggu.domain.model.PatientProfile
import com.ruangtunggu.domain.model.PoliItem
import com.ruangtunggu.domain.repository.AuthRepository
import com.ruangtunggu.domain.repository.QueueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class KonfirmasiPendaftaranUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val patientProfile: PatientProfile = PatientProfile(),
    val isSlotFullPreview: Boolean = false,
    val remainingQuotaSlots: Int = 12,
    val bookedQueue: ActiveQueue? = null
)

@HiltViewModel
class KonfirmasiPendaftaranViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val queueRepository: QueueRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(KonfirmasiPendaftaranUiState())
    val uiState: StateFlow<KonfirmasiPendaftaranUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val profile = authRepository.getCurrentProfile()
            val active = queueRepository.getOfflineCachedTicket()
            _uiState.update { it.copy(patientProfile = profile, bookedQueue = active) }
        }
    }

    fun toggleSlotFullPreview() {
        _uiState.update { it.copy(isSlotFullPreview = !it.isSlotFullPreview) }
    }

    fun confirmBooking(
        hospitalId: String,
        hospitalName: String,
        poliId: String,
        poliName: String,
        poliShortName: String,
        doctorId: String,
        doctorName: String,
        dateLabel: String,
        slotTime: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val hospital = HospitalItem(
                id = hospitalId,
                name = hospitalName,
                rating = 4.8,
                distanceKm = "1,2 km",
                drawableResName = "img_rs_sehat_sidoarjo"
            )
            val poli = PoliItem(
                id = poliId,
                name = poliName,
                shortName = poliShortName,
                doctorCount = 5,
                startHour = "08.00"
            )
            val doctor = DoctorScheduleItem(
                id = doctorId,
                name = doctorName,
                initials = "AL",
                specialty = "Spesialis $poliShortName",
                poliShortName = poliShortName,
                rating = 4.9,
                experienceYears = 6,
                slots = listOf(DoctorTimeSlot(slotTime))
            )
            val ticket = queueRepository.bookNewQueue(
                hospital = hospital,
                poli = poli,
                doctor = doctor,
                dateLabel = dateLabel,
                slotTime = slotTime
            )
            _uiState.update { it.copy(isLoading = false, bookedQueue = ticket) }
            onSuccess()
        }
    }
}
