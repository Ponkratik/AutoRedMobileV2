package com.ponkratov.autored.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ponkratov.autored.data.mapper.toData
import com.ponkratov.autored.domain.model.FirebaseUser
import com.ponkratov.autored.domain.model.response.JwtResponse
import com.ponkratov.autored.domain.repository.FirebaseUserRepository
import kotlinx.coroutines.tasks.await

class FirebaseUserRepositoryImpl(
    private val sharedPrefsRepositoryImpl: SharedPrefsRepositoryImpl
) : FirebaseUserRepository {

    override suspend fun register(firebaseUser: FirebaseUser): Result<AuthResult> = runCatching {
        val firebaseUserDTO = firebaseUser.toData()
        Firebase.auth.createUserWithEmailAndPassword(
            firebaseUserDTO.email,
            firebaseUserDTO.password
        )
            .await()
    }

    override suspend fun authenticate(firebaseUser: FirebaseUser): Result<AuthResult> =
        runCatching {
            val firebaseUserDTO = firebaseUser.toData()
            Firebase.auth.signInWithEmailAndPassword(
                firebaseUserDTO.email,
                firebaseUserDTO.password
            )
                .addOnCompleteListener {
                    Firebase.auth.currentUser?.getIdToken(true)
                        ?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                sharedPrefsRepositoryImpl.putIdToken(requireNotNull(it.result.token))
                                sharedPrefsRepositoryImpl.putJwt(
                                    JwtResponse(
                                        it.result.token.toString(),
                                        Firebase.auth.currentUser?.uid.toString(),
                                        "",
                                        Firebase.auth.currentUser?.email.toString(),
                                        Firebase.auth.currentUser?.phoneNumber.toString(),
                                        it.result.claims.map { entry ->
                                            entry.value.toString()
                                        }
                                    )
                                )
                            }
                        }
                }
                .await()
        }

    override suspend fun resetPassword(email: String): Result<Unit> = runCatching {
        Firebase.auth.sendPasswordResetEmail(email).addOnCompleteListener {}.await()
    }

    override suspend fun sendVerificationEmail(): Result<Unit> = runCatching {
        Firebase.auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {}?.await()
    }
}