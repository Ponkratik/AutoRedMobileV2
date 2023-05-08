package com.ponkratov.autored.data.api

import com.ponkratov.autored.data.model.request.RegisterRequestDTO
import com.ponkratov.autored.data.model.response.MessageResponseDTO
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApi {

    @Multipart
    @POST("auth/register")
    suspend fun register(
        @Part("registerRequest") registerRequest: RegisterRequestDTO,
        @Part avatarPhoto: MultipartBody.Part,
        @Part passportPhoto: MultipartBody.Part,
        @Part driverLicensePhoto: MultipartBody.Part
    ): MessageResponseDTO
}