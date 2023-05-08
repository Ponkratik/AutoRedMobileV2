package com.ponkratov.autored.domain.model

data class SupportRequest(
    val id: String = "",
    val userId: String,
    val message: String
)