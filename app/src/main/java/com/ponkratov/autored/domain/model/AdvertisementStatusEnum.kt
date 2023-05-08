package com.ponkratov.autored.domain.model

enum class AdvertisementStatusEnum(val desc: String) {
    STATUS_MODERATING("На модерации"),
    STATUS_ACCEPTED("Принято"),
    STATUS_REJECTED("Отклонено")
}