package com.ponkratov.autored.data.model

import java.util.*

data class UserDTO(
    val id: String,
    val fio: String,
    val email: String,
    val phone: String,
    val birthdate: Date,
    val profileDescription: String,
    val passportNumber: String,
    val driverLicenseNumber: String
)