package com.ponkratov.autored.data.model

import java.util.*

data class AdvertisementDTO(
    val id: String,
    val userId: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val publicationDate: Date,
    val carId: String,
    val pricePerDay: Double,
    val pricePerWeek: Double,
    val pricePerMonth: Double,
    val statusId: Long,
    val message: String
)