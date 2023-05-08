package com.ponkratov.autored.data.mapper

import com.ponkratov.autored.data.model.FirebaseUserDTO
import com.ponkratov.autored.domain.model.FirebaseUser

fun FirebaseUserDTO.toDomain(): FirebaseUser {
    return FirebaseUser(email, password)
}

fun FirebaseUser.toData(): FirebaseUserDTO {
    return FirebaseUserDTO(email, password)
}