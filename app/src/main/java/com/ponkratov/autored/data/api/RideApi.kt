package com.ponkratov.autored.data.api

import com.ponkratov.autored.data.model.response.MessageResponseDTO
import com.ponkratov.autored.data.model.response.RideResponseDTO
import com.ponkratov.autored.domain.model.request.BookRequest
import okhttp3.MultipartBody
import retrofit2.http.*

interface RideApi {

    @POST("ride/book")
    suspend fun bookRide(@Body bookRequest: BookRequest): MessageResponseDTO

    @POST("sign/before/lessor/{id}")
    suspend fun signActBeforeByLessor(
        @Path("rideId") rideId: String
    ): MessageResponseDTO

    @POST("sign/before/lessee/{id}")
    suspend fun signActBeforeByLessee(
        @Path("rideId") rideId: String,
        @Part files: List<MultipartBody.Part>
    ): MessageResponseDTO

    @POST("sign/after/lessor/{id}")
    suspend fun signActAfterByLessor(
        @Path("rideId") rideId: String,
        @Part files: List<MultipartBody.Part>
    ): MessageResponseDTO

    @POST("sign/after/lessee/{id}")
    suspend fun signActAfterByLessee(
        @Path("rideId") rideId: String
    ): MessageResponseDTO

    @GET("ride/get/all/full/advertisement/{id}")
    suspend fun getRideResponsesByAdvertisementId(@Path("id") advertisementId: String): List<RideResponseDTO>

    @GET("ride/get/all/full/lessor/{id}")
    suspend fun getRideResponsesByLessorId(@Path("id") lessorId: String): List<RideResponseDTO>
}