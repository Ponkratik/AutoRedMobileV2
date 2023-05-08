package com.ponkratov.autored.domain.usecase

import com.ponkratov.autored.domain.repository.FirebaseUserRepository

class ResetPasswordUseCase(
    private val firebaseUserRepository: FirebaseUserRepository
) {

    suspend operator fun invoke(email: String): Result<Unit> =
        firebaseUserRepository.resetPassword(email)
}