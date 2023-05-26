package com.ponkratov.autored.domain.usecase

import com.ponkratov.autored.domain.model.request.BookRequest
import com.ponkratov.autored.domain.repository.RideRepository

class BookRideUseCase(
    private val rideRepository: RideRepository
) {

    suspend operator fun invoke(
        bookRequest: BookRequest
    ) = rideRepository.bookRide(bookRequest)
}