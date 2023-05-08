package com.ponkratov.autored.data.mapper

import com.ponkratov.autored.data.model.AdvertisementDTO
import com.ponkratov.autored.data.model.response.AdvertisementResponseDTO
import com.ponkratov.autored.domain.model.Advertisement
import com.ponkratov.autored.domain.model.response.AdvertisementResponse

fun AdvertisementDTO.toDomain(): Advertisement {
    return Advertisement(
        id,
        userId,
        location,
        latitude,
        longitude,
        publicationDate,
        carId,
        pricePerDay,
        pricePerWeek,
        pricePerMonth,
        statusId,
        message
    )
}

fun Advertisement.toData(): AdvertisementDTO {
    return AdvertisementDTO(
        id,
        userId,
        location,
        latitude,
        longitude,
        publicationDate,
        carId,
        pricePerDay,
        pricePerWeek,
        pricePerMonth,
        statusId,
        message
    )
}

fun AdvertisementResponseDTO.toDomain(): AdvertisementResponse {
    return AdvertisementResponse(
        advertisement.toDomain(),
        car.toDomain(),
        carFeatureList.toDomain(),
        photoPaths,
        avgMark,
        rides
    )
}

fun AdvertisementResponse.toData(): AdvertisementResponseDTO {
    return AdvertisementResponseDTO(
        advertisement.toData(), car.toData(), carFeatureList.toData(), photoPaths, avgMark, rides
    )
}