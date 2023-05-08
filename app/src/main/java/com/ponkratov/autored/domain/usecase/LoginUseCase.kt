package com.ponkratov.autored.domain.usecase

import com.google.firebase.auth.AuthResult
import com.ponkratov.autored.domain.model.FirebaseUser
import com.ponkratov.autored.domain.repository.FirebaseUserRepository

class LoginUseCase(
    private val firebaseUserRepository: FirebaseUserRepository
) {
    suspend operator fun invoke(firebaseUser: FirebaseUser): Result<AuthResult> =
        firebaseUserRepository.authenticate(firebaseUser)
}