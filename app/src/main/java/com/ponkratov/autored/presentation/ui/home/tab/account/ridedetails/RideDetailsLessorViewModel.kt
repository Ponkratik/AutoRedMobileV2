package com.ponkratov.autored.presentation.ui.home.tab.account.ridedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ponkratov.autored.domain.model.Lce
import com.ponkratov.autored.domain.model.response.RideResponse
import com.ponkratov.autored.domain.usecase.GetRideResponseByIdUseCase
import com.ponkratov.autored.domain.usecase.SignActAfterByLessorUseCase
import com.ponkratov.autored.domain.usecase.SignActBeforeByLessorUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File

class RideDetailsLessorViewModel(
    private val getRideResponseByIdUseCase: GetRideResponseByIdUseCase,
    private val signActAfterByLessorUseCase: SignActAfterByLessorUseCase,
    private val signActBeforeByLessorUseCase: SignActBeforeByLessorUseCase
) : ViewModel() {

    private val initFlowInfo = MutableSharedFlow<String>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val initFlowSignActBefore = MutableSharedFlow<String>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val initFlowSignAfter = MutableSharedFlow<String>(
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
            signActBeforeByLessorUseCase(id)
                .fold(
                    onSuccess = {
                        updateInfo(id)
                    },
                    onFailure = {
                        lceFlowInfo.tryEmit(Lce.Error(it.message))
                    }
                )
        }.launchIn(viewModelScope)

    private val networkFlowSignAfter = initFlowSignAfter
        .onEach {
            lceFlowInfo.tryEmit(Lce.Loading())
        }
        .onEach { id ->
            signActAfterByLessorUseCase(id, files)
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

    fun signActBefore(id: String) {
        initFlowSignActBefore.tryEmit(id)
    }

    fun signActAfter(id: String, files: List<File>) {
        this.files = files
        initFlowSignAfter.tryEmit(id)
    }
}