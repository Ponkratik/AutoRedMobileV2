package com.ponkratov.autored.presentation.ui.home.tab.account.ridelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ponkratov.autored.domain.model.Lce
import com.ponkratov.autored.domain.model.response.RideResponse
import com.ponkratov.autored.domain.usecase.GetRideResponsesByAdvertisementIdUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class RideListLessorViewModel(
    private val getRideResponsesByAdvertisementIdUseCase: GetRideResponsesByAdvertisementIdUseCase
) : ViewModel() {

    private val initFlow = MutableSharedFlow<String>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val lceFlow = MutableSharedFlow<Lce<List<RideResponse>>>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val networkFlow = initFlow
        .onEach {
            lceFlow.tryEmit(Lce.Loading())
        }
        .onEach { advertisementId ->
            getRideResponsesByAdvertisementIdUseCase(advertisementId).fold(
                onSuccess = {
                    lceFlow.tryEmit(Lce.Content(it))
                },
                onFailure = {
                    lceFlow.tryEmit(Lce.Error(it.message))
                }
            )
        }.launchIn(viewModelScope)

    fun onRefreshSwiped(advertisementId: String) {
        initFlow.tryEmit(advertisementId)
    }
}