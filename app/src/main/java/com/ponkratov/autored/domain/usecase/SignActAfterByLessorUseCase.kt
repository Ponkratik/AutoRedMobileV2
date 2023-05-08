package com.ponkratov.autored.domain.usecase

import com.ponkratov.autored.domain.repository.RideRepository
import java.io.File

class SignActAfterByLessorUseCase(
    private val rideRepository: RideRepository
) {

    suspend operator fun invoke(rideId: String, files: List<File>) =
        rideRepository.signActAfterByLessor(rideId, files)
}