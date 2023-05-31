package com.ponkratov.autored.data.api

import com.ponkratov.autored.data.model.response.MessageResponseDTO
import com.ponkratov.autored.data.model.response.RideResponseDTO
import com.ponkratov.autored.domain.model.request.BookRequest
import okhttp3.MultipartBody
import retrofit2.http.*

interface RideApi {

    @POST("ride/book")
    suspend fun bookRide(@Body bookRequest: BookRequest): MessageResponseDTO

    @POST("ride/sign/before/lessor/{id}")
    suspend fun signActBeforeByLessor(
        @Path("id") rideId: String
    ): MessageResponseDTO

    @Multipart
    @POST("ride/sign/before/lessee/{id}")
    suspend fun signActBeforeByLessee(
        @Path("id") rideId: String,
        @Part files: List<MultipartBody.Part>
    ): MessageResponseDTO

    @Multipart
    @POST("ride/sign/after/lessor/{id}")
    suspend fun signActAfterByLessor(
        @Path("id") rideId: String,
        @Part files: List<MultipartBody.Part>
    ): MessageResponseDTO

    @POST("ride/sign/after/lessee/{id}")
    suspend fun signActAfterByLessee(
        @Path("id") rideId: String
    ): MessageResponseDTO

    @GET("ride/get/all/full/advertisement/{id}")
    suspend fun getRideResponsesByAdvertisementId(@Path("id") advertisementId: String): List<RideResponseDTO>

    @GET("ride/get/all/full/lessor/{id}")
    suspend fun getRideResponsesByLessorId(@Path("id") lessorId: String): List<RideResponseDTO>

    @GET("ride/get/full/{id}")
    suspend fun getRideResponseById(@Path("id") rideId: String): RideResponseDTO
}