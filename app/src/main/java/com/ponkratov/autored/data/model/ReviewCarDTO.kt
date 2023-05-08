package com.ponkratov.autored.data.model

data class ReviewCarDTO(
    val id: String,
    val mark: Int,
    val comment: String,
    val userFrom: String,
    val carTo: String
)