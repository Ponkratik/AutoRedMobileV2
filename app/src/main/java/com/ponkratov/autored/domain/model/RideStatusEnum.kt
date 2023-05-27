package com.ponkratov.autored.domain.model

enum class RideStatusEnum(val id: Long, val desc: String) {
    STATUS_BOOKED(1, "Автомобиль забронирован"),
    STATUS_SIGNED_BEFORE_LESSOR(2, "Подписан арендатором"),
    STATUS_SIGNED_BEFORE_LESSEE(3, "Подписан арендодателем"),
    STATUS_STARTED(4, "Начата"),
    STATUS_SIGNED_AFTER_LESSOR(5, "Подписан арендатором"),
    STATUS_SIGNED_AFTER_LESSEE(6, "Подписан арендодателем"),
    STATUS_FINISHED(7, "Завершена"),
    STATUS_CANCELLED(8, "Отменена")
}