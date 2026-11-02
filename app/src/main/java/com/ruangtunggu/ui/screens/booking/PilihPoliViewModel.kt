package com.ruangtunggu.ui.screens.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruangtunggu.domain.model.PoliItem
import com.ruangtunggu.domain.repository.QueueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PilihPoliUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val poliList: List<PoliItem> = emptyList(),
    val selectedPoli: PoliItem? = null
)

@HiltViewModel
class PilihPoliViewModel @Inject constructor(
    private val queueRepository: QueueRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PilihPoliUiState())
    val uiState: StateFlow<PilihPoliUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val list = queueRepository.getAvailablePoliList()
            _uiState.update { it.copy(isLoading = false, poliList = list) }
        }
    }

    fun onPoliSelected(poli: PoliItem) {
        _uiState.update { it.copy(selectedPoli = poli) }
    }
}
