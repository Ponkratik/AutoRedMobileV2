package com.ponkratov.autored.domain.model

import java.util.Date

data class Advertisement(
    val id: String = "",
    val userId: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val publicationDate: Date = Date(),
    val carId: String = "",
    val pricePerDay: Double,
    val pricePerWeek: Double,
    val pricePerMonth: Double,
    val statusId: Long = 0,
    val message: String = ""
)