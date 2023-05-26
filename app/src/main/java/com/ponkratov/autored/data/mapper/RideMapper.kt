package com.ponkratov.autored.data.mapper

import com.ponkratov.autored.data.model.RideDTO
import com.ponkratov.autored.data.model.request.BookRequestDTO
import com.ponkratov.autored.domain.model.Ride
import com.ponkratov.autored.domain.model.request.BookRequest

fun RideDTO.toDomain(): Ride {
    return Ride(
        id,
        advertisementId,
        lessorId,
        dateBook,
        dateStart,
        dateEnd,
        dateSignedBeforeLessor,
        dateSignedBeforeLessee,
        dateSignedAfterLessor,
        dateSignedAfterLessee,
        chatLink,
        paymentLink,
        paymentDate,
        totalCost,
        statusId
    )
}

fun Ride.toData(): RideDTO {
    return RideDTO(
        id,
        advertisementId,
        lessorId,
        dateBook,
        dateStart,
        dateEnd,
        dateSignedBeforeLessor,
        dateSignedBeforeLessee,
        dateSignedAfterLessor,
        dateSignedAfterLessee,
        chatLink,
        paymentLink,
        paymentDate,
        totalCost,
        statusId
    )
}

fun BookRequest.toData(): BookRequestDTO {
    return BookRequestDTO(advertisementId, lesseeId, dateStart, dateEnd)
}

fun BookRequestDTO.toDomain(): BookRequest {
    return BookRequest(advertisementId, lesseeId, dateStart, dateEnd)
}