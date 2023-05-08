package com.ponkratov.autored.domain.usecase

import com.ponkratov.autored.domain.repository.RideRepository
import java.io.File

class SignActBeforeByLesseeUseCase(
    private val rideRepository: RideRepository
) {

    suspend operator fun invoke(rideId: String, files: List<File>) =
        rideRepository.signActBeforeByLessee(rideId, files)
}