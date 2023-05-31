package com.ponkratov.autored.domain.model

enum class RideStatusEnum(val id: Long, val desc: String) {
    STATUS_BOOKED(1, "Автомобиль забронирован"),
    STATUS_SIGNED_BEFORE_LESSEE(2, "Подписан арендодателем"),
    STATUS_PAYED(3, "Оплачен"),
    STATUS_STARTED(4, "Начата"),
    STATUS_SIGNED_AFTER_LESSOR(5, "Подписан арендатором"),
    STATUS_FINISHED(6, "Завершена"),
    STATUS_CANCELLED(7, "Отменена")
}