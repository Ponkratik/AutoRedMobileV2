package com.ponkratov.autored.data.model

data class ReviewUserDTO(
    val id: String,
    val mark: Int,
    val comment: String,
    val userFrom: String,
    val userTo: String
)