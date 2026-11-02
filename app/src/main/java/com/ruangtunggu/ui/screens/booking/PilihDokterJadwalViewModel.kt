package com.ruangtunggu.ui.screens.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruangtunggu.domain.model.DoctorScheduleItem
import com.ruangtunggu.domain.repository.QueueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DayDateOption(
    val dayShort: String,
    val dayNumber: String,
    val fullLabel: String
)

data class PilihDokterJadwalUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val dates: List<DayDateOption> = listOf(
        DayDateOption("Kam", "22", "Kam, 22 Okt"),
        DayDateOption("Jum", "23", "Jum, 23 Okt"),
        DayDateOption("Sab", "24", "Sab, 24 Okt"),
        DayDateOption("Min", "25", "Min, 25 Okt"),
        DayDateOption("Sen", "26", "Sen, 26 Okt")
    ),
    val selectedDateIndex: Int = 0,
    val doctors: List<DoctorScheduleItem> = emptyList(),
    val selectedDoctor: DoctorScheduleItem? = null,
    val selectedSlotTime: String? = null,
    val fullSlotAlertDoctorId: String? = null
)

@HiltViewModel
class PilihDokterJadwalViewModel @Inject constructor(
    private val queueRepository: QueueRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PilihDokterJadwalUiState())
    val uiState: StateFlow<PilihDokterJadwalUiState> = _uiState.asStateFlow()

    private var currentPoliShort = "Anak"

    fun loadDoctors(poliShortName: String, dateIndex: Int = 0) {
        currentPoliShort = poliShortName
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, selectedDateIndex = dateIndex) }
            delay(320)
            val list = queueRepository.getDoctorsForPoli(poliShortName, dateIndex)
            val firstDoc = list.firstOrNull()
            val firstOpenSlot = firstDoc?.slots?.firstOrNull { !it.isFull }?.time
            _uiState.update {
                it.copy(
                    isLoading = false,
                    doctors = list,
                    selectedDoctor = firstDoc,
                    selectedSlotTime = firstOpenSlot,
                    fullSlotAlertDoctorId = null
                )
            }
        }
    }

    fun onDateSelected(index: Int) {
        loadDoctors(currentPoliShort, index)
    }

    fun onSlotClicked(doctor: DoctorScheduleItem, slotTime: String, isFull: Boolean) {
        if (isFull) {
            // Trigger Smart Full-Slot Suggestion Banner (#3) instead of a dead-end!
            _uiState.update { it.copy(fullSlotAlertDoctorId = doctor.id) }
        } else {
            _uiState.update {
                it.copy(
                    selectedDoctor = doctor,
                    selectedSlotTime = slotTime,
                    fullSlotAlertDoctorId = null
                )
            }
        }
    }

    /**
     * Smart Suggestion Action 1: "Slot hari ini penuh. Lihat jadwal Besok (09:00)"
     */
    fun onApplyTomorrowSmartSuggestion(doctorId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, selectedDateIndex = 1, fullSlotAlertDoctorId = null) }
            delay(250)
            val tomorrowList = queueRepository.getDoctorsForPoli(currentPoliShort, dateIndex = 1)
            val targetDoc = tomorrowList.find { it.id == doctorId } ?: tomorrowList.firstOrNull()
            val nineAmSlot = targetDoc?.slots?.firstOrNull { it.time == "09:00" || !it.isFull }?.time ?: "09:00"
            _uiState.update {
                it.copy(
                    isLoading = false,
                    doctors = tomorrowList,
                    selectedDoctor = targetDoc,
                    selectedSlotTime = nineAmSlot
                )
            }
        }
    }

    /**
     * Smart Suggestion Action 2: "Atau lihat dokter lain di Poli yang sama"
     */
    fun onSelectAlternativeDoctorInSamePoli(currentDoctorId: String) {
        val currentList = _uiState.value.doctors
        val alternative = currentList.firstOrNull { doc ->
            doc.id != currentDoctorId && doc.slots.any { !it.isFull }
        } ?: currentList.firstOrNull()

        val openSlot = alternative?.slots?.firstOrNull { !it.isFull }?.time
        _uiState.update {
            it.copy(
                selectedDoctor = alternative,
                selectedSlotTime = openSlot,
                fullSlotAlertDoctorId = null
            )
        }
    }
}
