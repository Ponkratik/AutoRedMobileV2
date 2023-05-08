package com.ponkratov.autored.domain.repository

import com.ponkratov.autored.domain.model.response.RideResponse
import retrofit2.http.Path
import java.io.File
import java.util.Date

interface RideRepository {

    suspend fun bookRide(
        advertisementId: String,
        lessorId: String,
        dateStart: Date,
        dateEnd: Date
    ): Result<String>

    suspend fun signActBeforeByLessor(rideId: String): Result<String>

    suspend fun signActBeforeByLessee(rideId: String, files: List<File>): Result<String>

    suspend fun signActAfterByLessor(rideId: String, files: List<File>): Result<String>

    suspend fun signActAfterByLessee(rideId: String): Result<String>

    suspend fun getRideResponsesByAdvertisementId(@Path("id") advertisementId: String): Result<List<RideResponse>>

    suspend fun getRideResponsesByLessorId(@Path("id") lessorId: String): Result<List<RideResponse>>

}