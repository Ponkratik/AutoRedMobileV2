package com.ponkratov.autored.presentation.ui.home.tab.history.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ponkratov.autored.domain.model.Lce
import com.ponkratov.autored.domain.model.ReviewCar
import com.ponkratov.autored.domain.model.ReviewUser
import com.ponkratov.autored.domain.usecase.AddReviewCarUseCase
import com.ponkratov.autored.domain.usecase.AddReviewUserUseCase
import com.ponkratov.autored.domain.usecase.GetJwtResponseUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AddReviewLesseeViewModel(
    private val addReviewUserUseCase: AddReviewUserUseCase,
    private val addReviewCarUseCase: AddReviewCarUseCase,
    private val getJwtResponseUseCase: GetJwtResponseUseCase
) : ViewModel() {

    private val initFlow = MutableSharedFlow<Unit>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val lceFlow = MutableSharedFlow<Lce<String>>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val networkFlow = initFlow
        .onEach {
            lceFlow.tryEmit(Lce.Loading())
        }
        .onEach {
            addReviewUserUseCase(requireNotNull(reviewUser))
                .fold(
                    onSuccess = {
                        addReviewCarUseCase(requireNotNull(reviewCar))
                            .fold(
                                onSuccess = {
                                    lceFlow.tryEmit(Lce.Content(it))
                                },
                                onFailure = {
                                    lceFlow.tryEmit(Lce.Error(it.message))
                                }
                            )
                    },
                    onFailure = {
                        lceFlow.tryEmit(Lce.Error(it.message))
                    }
                )
        }.launchIn(viewModelScope)

    private var reviewUser: ReviewUser? = null
    private var reviewCar: ReviewCar? = null

    fun onSendButtonClicked(
        markCar: Int,
        commentCar: String,
        carTo: String,
        markUser: Int,
        commentUser: String,
        userTo: String
    ) {
        reviewUser = ReviewUser(
            mark = markUser,
            comment = commentUser,
            userFrom = getJwtResponseUseCase().id,
            userTo = userTo
        )

        reviewCar = ReviewCar(
            mark = markCar,
            comment = commentCar,
            userFrom = getJwtResponseUseCase().id,
            carTo = carTo
        )

        initFlow.tryEmit(Unit)
    }
}