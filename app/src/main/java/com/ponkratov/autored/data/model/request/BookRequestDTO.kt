package com.ponkratov.autored.data.model.request

import java.util.Date

data class BookRequestDTO(
    val advertisementId: String,
    val lesseeId: String,
    val dateStart: Date,
    val dateEnd: Date
)