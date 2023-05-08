package com.ponkratov.autored.data.mapper

import com.ponkratov.autored.data.model.RideDTO
import com.ponkratov.autored.domain.model.Ride

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
        totalCost
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
        totalCost
    )
}