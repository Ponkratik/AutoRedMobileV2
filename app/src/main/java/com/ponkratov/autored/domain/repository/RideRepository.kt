package com.ponkratov.autored.domain.repository

import com.ponkratov.autored.domain.model.request.BookRequest
import com.ponkratov.autored.domain.model.response.RideResponse
import retrofit2.http.Path
import java.io.File

interface RideRepository {

    suspend fun bookRide(bookRequest: BookRequest): Result<String>

    suspend fun signActBeforeByLessor(rideId: String): Result<String>

    suspend fun signActBeforeByLessee(rideId: String, files: List<File>): Result<String>

    suspend fun signActAfterByLessor(rideId: String, files: List<File>): Result<String>

    suspend fun signActAfterByLessee(rideId: String): Result<String>

    suspend fun getRideResponsesByAdvertisementId(advertisementId: String): Result<List<RideResponse>>

    suspend fun getRideResponsesByLessorId(lessorId: String): Result<List<RideResponse>>

    suspend fun getRideResponseById(id: String): Result<RideResponse>
}