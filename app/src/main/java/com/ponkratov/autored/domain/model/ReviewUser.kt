package com.ponkratov.autored.domain.model

data class ReviewUser(
    val id: String = "",
    val mark: Int,
    val comment: String,
    val userFrom: String,
    val userTo: String
)