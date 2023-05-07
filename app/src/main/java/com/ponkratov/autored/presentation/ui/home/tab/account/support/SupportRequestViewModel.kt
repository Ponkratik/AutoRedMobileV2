package com.ponkratov.autored.presentation.ui.home.tab.account.support

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ponkratov.autored.domain.model.Lce
import com.ponkratov.autored.domain.model.SupportRequest
import com.ponkratov.autored.domain.usecase.CreateSupportRequestUseCase
import com.ponkratov.autored.domain.usecase.GetJwtResponseUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SupportRequestViewModel(
    private val createSupportRequestUseCase: CreateSupportRequestUseCase,
    private val getJwtResponseUseCase: GetJwtResponseUseCase
) : ViewModel() {

    private val initFlow = MutableSharedFlow<SupportRequest>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val lceFlow = MutableSharedFlow<Lce<String>>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val networkFlow = initFlow
        .onEach {
            lceFlow.tryEmit(Lce.Loading())
        }
        .onEach { request ->
            createSupportRequestUseCase(request).fold(
                onSuccess = {
                    lceFlow.tryEmit(Lce.Success(it))
                },
                onFailure = {
                    lceFlow.tryEmit(Lce.Error(it.message))
                }
            )
        }.launchIn(viewModelScope)

    fun onSendButtonClicked(message: String) {
        initFlow.tryEmit(
            SupportRequest(
                userId = getJwtResponseUseCase().id, message = message
            )
        )
    }
}