package com.ponkratov.autored.domain.repository

import com.google.firebase.auth.AuthResult
import com.ponkratov.autored.domain.model.FirebaseUser

interface FirebaseUserRepository {

    suspend fun register(firebaseUser: FirebaseUser): Result<AuthResult>

    suspend fun authenticate(firebaseUser: FirebaseUser): Result<AuthResult>

    suspend fun resetPassword(email: String): Result<Unit>

    suspend fun sendVerificationEmail(): Result<Unit>
}