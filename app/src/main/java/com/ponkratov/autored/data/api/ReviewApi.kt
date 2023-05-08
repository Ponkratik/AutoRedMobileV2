package com.ponkratov.autored.data.api

import com.ponkratov.autored.data.model.ReviewCarDTO
import com.ponkratov.autored.data.model.ReviewUserDTO
import com.ponkratov.autored.data.model.response.MessageResponseDTO
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ReviewApi {

    @Multipart
    @POST("review/car/add")
    suspend fun addCarReview(
        @Part("review") reviewCar: ReviewCarDTO
    ): MessageResponseDTO

    @Multipart
    @POST("review/user/add")
    suspend fun addUserReview(
        @Part("review") reviewUser: ReviewUserDTO
    ): MessageResponseDTO
}