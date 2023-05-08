package com.ponkratov.autored.domain.usecase

import com.ponkratov.autored.domain.repository.SharedPrefsRepository

class GetIdTokenUseCase(
    private val sharedPrefsRepository: SharedPrefsRepository
) {
    operator fun invoke(): String = sharedPrefsRepository.getIdToken()
}
