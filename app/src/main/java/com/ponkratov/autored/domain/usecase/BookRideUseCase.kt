package com.ponkratov.autored.domain.usecase

import com.ponkratov.autored.domain.repository.RideRepository
import java.util.Date

class BookRideUseCase(
    private val rideRepository: RideRepository
) {

    suspend operator fun invoke(
        advertisementId: String,
        lessorId: String,
        dateStart: Date,
        dateEnd: Date
    ) = rideRepository.bookRide(advertisementId, lessorId, dateStart, dateEnd)
}