package com.ponkratov.autored.domain.model.request

import java.util.Date

data class BookRequest(
    val advertisementId: String,
    val lesseeId: String,
    val dateStart: Date,
    val dateEnd: Date
)