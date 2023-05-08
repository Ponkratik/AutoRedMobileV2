package com.ponkratov.autored.domain.model

import java.util.*

data class Car(
    val id: String = "",
    val vin: String,
    val licensePlate: String,
    val make: String,
    val model: String,
    val manufacturedYear: Date,
    val transmissionType: String,
    val fuelType: String,
    val doors: Int,
    val seats: Int,
    val carType: String,
    val color: String
)