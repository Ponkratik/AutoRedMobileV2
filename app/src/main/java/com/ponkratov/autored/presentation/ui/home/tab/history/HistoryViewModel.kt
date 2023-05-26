package com.ponkratov.autored.presentation.ui.home.tab.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ponkratov.autored.domain.model.Lce
import com.ponkratov.autored.domain.model.response.RideResponse
import com.ponkratov.autored.domain.usecase.GetJwtResponseUseCase
import com.ponkratov.autored.domain.usecase.GetRideResponsesByLessorIdUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HistoryViewModel(
    private val getRideResponsesByLessorIdUseCase: GetRideResponsesByLessorIdUseCase,
    private val getJwtResponseUseCase: GetJwtResponseUseCase
) : ViewModel() {

    private val initFlow = MutableSharedFlow<Unit>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val lceFlow = MutableSharedFlow<Lce<List<RideResponse>>>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val networkFlow = initFlow
        .onEach {
            lceFlow.tryEmit(Lce.Loading())
        }
        .onEach {
            getRideResponsesByLessorIdUseCase(getJwtResponseUseCase().id).fold(
                onSuccess = {
                    lceFlow.tryEmit(Lce.Content(it))
                },
                onFailure = {
                    lceFlow.tryEmit(Lce.Error(it.message))
                }
            )
        }.launchIn(viewModelScope)

    init {
        initFlow.tryEmit(Unit)
    }

    fun onRefreshSwiped() {
        initFlow.tryEmit(Unit)
    }
}