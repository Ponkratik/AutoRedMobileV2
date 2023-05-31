package com.ponkratov.autored.presentation.ui.home.tab.account.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ponkratov.autored.domain.model.Lce
import com.ponkratov.autored.domain.model.ReviewUser
import com.ponkratov.autored.domain.usecase.AddReviewUserUseCase
import com.ponkratov.autored.domain.usecase.GetJwtResponseUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AddReviewLessorViewModel(
    private val addReviewUserUseCase: AddReviewUserUseCase,
    private val getJwtResponseUseCase: GetJwtResponseUseCase
) : ViewModel() {

    private val initFlow = MutableSharedFlow<ReviewUser>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val lceFlow = MutableSharedFlow<Lce<String>>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val networkFlow = initFlow
        .onEach {
            lceFlow.tryEmit(Lce.Loading())
        }
        .onEach { reviewUser ->
            addReviewUserUseCase(reviewUser).fold(
                onSuccess = {
                    lceFlow.tryEmit(Lce.Content(it))
                },
                onFailure = {
                    lceFlow.tryEmit(Lce.Error(it.message))
                }
            )
        }.launchIn(viewModelScope)

    fun onSendButtonClicked(markUser: Int, commentUser: String, userTo: String) {
        initFlow.tryEmit(
            ReviewUser(
                mark = markUser,
                comment = commentUser,
                userFrom = getJwtResponseUseCase().id,
                userTo = userTo
            )
        )
    }
}