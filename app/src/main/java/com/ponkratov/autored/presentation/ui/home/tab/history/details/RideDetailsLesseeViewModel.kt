package com.ponkratov.autored.presentation.ui.home.tab.history.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ponkratov.autored.domain.model.Lce
import com.ponkratov.autored.domain.model.response.RideResponse
import com.ponkratov.autored.domain.usecase.GetRideResponseByIdUseCase
import com.ponkratov.autored.domain.usecase.SignActAfterByLesseeUseCase
import com.ponkratov.autored.domain.usecase.SignActBeforeByLesseeUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File

class RideDetailsLesseeViewModel(
    private val getRideResponseByIdUseCase: GetRideResponseByIdUseCase,
    private val signActAfterByLesseeUseCase: SignActAfterByLesseeUseCase,
    private val signActBeforeByLesseeUseCase: SignActBeforeByLesseeUseCase
) : ViewModel() {

    private val initFlowInfo = MutableSharedFlow<String>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val initFlowSignActBefore = MutableSharedFlow<String>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val initFlowEndRide = MutableSharedFlow<String>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val lceFlowInfo = MutableSharedFlow<Lce<RideResponse>>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private var files: List<File> = emptyList()

    private val networkFlowInfo = initFlowInfo
        .onEach {
            lceFlowInfo.tryEmit(Lce.Loading())
        }
        .onEach { id ->
            getRideResponseByIdUseCase(id)
                .fold(
                    onSuccess = {
                        lceFlowInfo.tryEmit(Lce.Content(it))
                    },
                    onFailure = {
                        lceFlowInfo.tryEmit(Lce.Error(it.message))
                    }
                )
        }.launchIn(viewModelScope)

    private val networkFlowSignActBefore = initFlowSignActBefore
        .onEach {
            lceFlowInfo.tryEmit(Lce.Loading())
        }
        .onEach { id ->
            signActBeforeByLesseeUseCase(id, files)
                .fold(
                    onSuccess = {
                        updateInfo(id)
                    },
                    onFailure = {
                        lceFlowInfo.tryEmit(Lce.Error(it.message))
                    }
                )
        }.launchIn(viewModelScope)

    private val networkFlowEndRide = initFlowEndRide
        .onEach {
            lceFlowInfo.tryEmit(Lce.Loading())
        }
        .onEach { id ->
            signActAfterByLesseeUseCase(id)
                .fold(
                    onSuccess = {
                        updateInfo(id)
                    },
                    onFailure = {
                        lceFlowInfo.tryEmit(Lce.Error(it.message))
                    }
                )
        }.launchIn(viewModelScope)

    fun updateInfo(id: String) {
        initFlowInfo.tryEmit(id)
    }

    fun signActBefore(id: String, files: List<File>) {
        this.files = files
        initFlowSignActBefore.tryEmit(id)
    }

    fun endRide(id: String) {
        initFlowEndRide.tryEmit(id)
    }
}