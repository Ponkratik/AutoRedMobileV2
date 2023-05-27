package com.ponkratov.autored.domain.usecase

import com.ponkratov.autored.domain.repository.RideRepository

class GetRideResponseByIdUseCase(
    private val rideRepository: RideRepository
) {

    suspend operator fun invoke(id: String) = rideRepository.getRideResponseById(id)
}