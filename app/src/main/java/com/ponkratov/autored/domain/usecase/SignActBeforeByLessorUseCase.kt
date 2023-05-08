package com.ponkratov.autored.domain.usecase

import com.ponkratov.autored.domain.repository.RideRepository

class SignActBeforeByLessorUseCase(
    private val rideRepository: RideRepository
) {

    suspend operator fun invoke(rideId: String) = rideRepository.signActBeforeByLessor(rideId)
}