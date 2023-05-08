package com.ponkratov.autored.domain.usecase

import com.ponkratov.autored.domain.repository.FirebaseUserRepository

class SendVerificationEmailUseCase(
    private val firebaseUserRepository: FirebaseUserRepository
) {

    suspend operator fun invoke(): Result<Unit> =
        firebaseUserRepository.sendVerificationEmail()
}