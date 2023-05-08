package com.ponkratov.autored.domain.repository

import com.ponkratov.autored.domain.model.request.RegisterRequest
import java.io.File

interface AuthRepository {

    suspend fun register(
        registerRequest: RegisterRequest,
        profilePhoto: File,
        passportPhoto: File,
        driverLicensePhoto: File
    ) : Result<String>
}