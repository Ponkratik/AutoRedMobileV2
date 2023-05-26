package com.ponkratov.autored.data.model

import java.util.*

data class RideDTO(
    val id: String,
    val advertisementId: String,
    val lessorId: String,
    val dateBook: Date,
    val dateStart: Date,
    val dateEnd: Date,
    val dateSignedBeforeLessor: Date,
    val dateSignedBeforeLessee: Date,
    val dateSignedAfterLessor: Date,
    val dateSignedAfterLessee: Date,
    val chatLink: String,
    val paymentLink: String,
    val paymentDate: Date,
    val totalCost: Double,
    val statusId: Long = 0
)