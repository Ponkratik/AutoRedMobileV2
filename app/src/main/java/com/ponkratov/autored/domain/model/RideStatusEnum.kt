package com.ponkratov.autored.domain.model

enum class RideStatusEnum(val desc: String) {
    STATUS_BOOKED("Автомобиль забронирован"),
    STATUS_SIGNED_BEFORE_LESSOR("Подписан арендатором"),
    STATUS_SIGNED_BEFORE_LESSEE("Подписан арендодателем"),
    STATUS_STARTED("Начата"),
    STATUS_SIGNED_AFTER_LESSOR("Подписан арендатором"),
    STATUS_SIGNED_AFTER_LESSEE("Подписан арендодателем"),
    STATUS_FINISHED("Завершена"),
    STATUS_CANCELLED("Отменена")
}