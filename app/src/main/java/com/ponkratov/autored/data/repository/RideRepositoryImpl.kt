package com.ponkratov.autored.data.repository

import com.ponkratov.autored.data.api.RideApi
import com.ponkratov.autored.data.mapper.toDomain
import com.ponkratov.autored.domain.model.request.BookRequest
import com.ponkratov.autored.domain.model.response.RideResponse
import com.ponkratov.autored.domain.repository.RideRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.Date

class RideRepositoryImpl(
    private val rideApi: RideApi
) : RideRepository {
    override suspend fun bookRide(bookRequest: BookRequest): Result<String> =
        runCatching {
            rideApi.bookRide(bookRequest).message
        }

    override suspend fun signActBeforeByLessor(rideId: String): Result<String> = runCatching {
        rideApi.signActBeforeByLessor(rideId).message
    }

    override suspend fun signActBeforeByLessee(rideId: String, files: List<File>): Result<String> = runCatching {
        val fileBodys = files.map {
            MultipartBody.Part.createFormData(
                "files",
                it.name,
                it.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }

        rideApi.signActBeforeByLessee(rideId, fileBodys).message
    }

    override suspend fun signActAfterByLessor(rideId: String, files: List<File>): Result<String> = runCatching {
        val fileBodys = files.map {
            MultipartBody.Part.createFormData(
                "files",
                it.name,
                it.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }

        rideApi.signActAfterByLessor(rideId, fileBodys).message
    }

    override suspend fun signActAfterByLessee(rideId: String): Result<String> = runCatching {
        rideApi.signActAfterByLessee(rideId).message
    }

    override suspend fun getRideResponsesByAdvertisementId(advertisementId: String): Result<List<RideResponse>> =
        runCatching {
            rideApi.getRideResponsesByAdvertisementId(advertisementId).map {
                it.toDomain()
            }
        }

    override suspend fun getRideResponsesByLessorId(lessorId: String): Result<List<RideResponse>> =
        runCatching {
            rideApi.getRideResponsesByLessorId(lessorId).map {
                it.toDomain()
            }
        }

    override suspend fun getRideResponseById(id: String): Result<RideResponse> = runCatching {
        rideApi.getRideResponseById(id).toDomain()
    }
}