package com.ponkratov.autored.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ponkratov.autored.domain.model.FirebaseUser
import com.ponkratov.autored.domain.model.Lce
import com.ponkratov.autored.domain.usecase.LoginUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val initFlow = MutableSharedFlow<FirebaseUser>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val lceFlow = MutableSharedFlow<Lce<Unit>>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val networkFlow = initFlow
        .onEach {
            lceFlow.tryEmit(Lce.Loading())
        }
        .onEach { request ->
            loginUseCase(request).fold(
                onSuccess = {
                    lceFlow.tryEmit(Lce.Content(Unit))
                },
                onFailure = {
                    lceFlow.tryEmit(Lce.Error(it.localizedMessage))
                }
            )
        }.launchIn(viewModelScope)

    fun onLoginButtonClicked(email: String, password: String) {
        initFlow.tryEmit(FirebaseUser(email, password))
    }
}