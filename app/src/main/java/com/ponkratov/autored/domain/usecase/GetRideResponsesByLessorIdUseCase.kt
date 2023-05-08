package com.ponkratov.autored.domain.usecase

import com.ponkratov.autored.domain.repository.RideRepository

class GetRideResponsesByLessorIdUseCase(
    private val rideRepository: RideRepository
) {

    suspend operator fun invoke(lessorId: String) = rideRepository.getRideResponsesByLessorId(lessorId)
}