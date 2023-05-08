package com.ponkratov.autored.domain.model

data class ReviewCar(
    val id: String = "",
    val mark: Int,
    val comment: String,
    val userFrom: String,
    val carTo: String
)