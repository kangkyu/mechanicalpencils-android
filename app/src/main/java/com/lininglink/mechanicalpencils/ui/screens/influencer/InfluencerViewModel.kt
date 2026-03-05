package com.lininglink.mechanicalpencils.ui.screens.influencer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lininglink.mechanicalpencils.data.model.Influencer
import com.lininglink.mechanicalpencils.data.model.InfluencerDetail
import com.lininglink.mechanicalpencils.data.repository.InfluencerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class InfluencerListUiState {
    data object Loading : InfluencerListUiState()
    data class Success(val influencers: List<Influencer>) : InfluencerListUiState()
    data class Error(val message: String) : InfluencerListUiState()
}

class InfluencerListViewModel(
    private val influencerRepository: InfluencerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<InfluencerListUiState>(InfluencerListUiState.Loading)
    val uiState: StateFlow<InfluencerListUiState> = _uiState.asStateFlow()

    init {
        loadInfluencers()
    }

    fun loadInfluencers() {
        viewModelScope.launch {
            _uiState.value = InfluencerListUiState.Loading
            influencerRepository.getInfluencers()
                .onSuccess { response ->
                    _uiState.value = InfluencerListUiState.Success(response.influencers)
                }
                .onFailure { error ->
                    _uiState.value = InfluencerListUiState.Error(error.message ?: "Failed to load influencers")
                }
        }
    }
}

sealed class InfluencerDetailUiState {
    data object Loading : InfluencerDetailUiState()
    data class Success(val influencer: InfluencerDetail) : InfluencerDetailUiState()
    data class Error(val message: String) : InfluencerDetailUiState()
}

class InfluencerDetailViewModel(
    private val influencerId: Int,
    private val influencerRepository: InfluencerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<InfluencerDetailUiState>(InfluencerDetailUiState.Loading)
    val uiState: StateFlow<InfluencerDetailUiState> = _uiState.asStateFlow()

    init {
        loadInfluencer()
    }

    fun loadInfluencer() {
        viewModelScope.launch {
            _uiState.value = InfluencerDetailUiState.Loading
            influencerRepository.getInfluencer(influencerId)
                .onSuccess { influencer ->
                    _uiState.value = InfluencerDetailUiState.Success(influencer)
                }
                .onFailure { error ->
                    _uiState.value = InfluencerDetailUiState.Error(error.message ?: "Failed to load influencer")
                }
        }
    }
}
