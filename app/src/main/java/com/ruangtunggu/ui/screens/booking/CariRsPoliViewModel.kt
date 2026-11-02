package com.ruangtunggu.ui.screens.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruangtunggu.domain.model.HospitalItem
import com.ruangtunggu.domain.repository.QueueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CariRsPoliUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val selectedFilterChip: String = "Semua",
    val filterChips: List<String> = listOf("Semua", "RS Umum", "Poli Anak", "Kandungan"),
    val hospitals: List<HospitalItem> = emptyList(),
    val selectedHospital: HospitalItem? = null
)

@HiltViewModel
class CariRsPoliViewModel @Inject constructor(
    private val queueRepository: QueueRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CariRsPoliUiState())
    val uiState: StateFlow<CariRsPoliUiState> = _uiState.asStateFlow()

    init {
        loadHospitals(showShimmer = true)
    }

    fun initializeFilter(initialFilter: String) {
        if (initialFilter.isNotBlank() && initialFilter != _uiState.value.selectedFilterChip) {
            _uiState.update { it.copy(selectedFilterChip = initialFilter) }
            loadHospitals(showShimmer = false)
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query, errorMessage = null) }
        loadHospitals(showShimmer = false)
    }

    fun onFilterChipSelected(chip: String) {
        _uiState.update { it.copy(selectedFilterChip = chip, errorMessage = null) }
        loadHospitals(showShimmer = true)
    }

    fun onHospitalSelected(hospital: HospitalItem) {
        _uiState.update { it.copy(selectedHospital = hospital) }
    }

    fun onResetSearch() {
        _uiState.update { it.copy(searchQuery = "", selectedFilterChip = "Semua", errorMessage = null) }
        loadHospitals(showShimmer = true)
    }

    private fun loadHospitals(showShimmer: Boolean) {
        viewModelScope.launch {
            if (showShimmer) {
                _uiState.update { it.copy(isLoading = true) }
                delay(420)
            }
            val current = _uiState.value
            val result = queueRepository.getNearbyHospitals(
                query = current.searchQuery,
                poliFilter = current.selectedFilterChip
            )
            _uiState.update {
                it.copy(
                    isLoading = false,
                    hospitals = result,
                    selectedHospital = result.firstOrNull { h -> h.id == it.selectedHospital?.id }
                        ?: result.firstOrNull()
                )
            }
        }
    }
}
