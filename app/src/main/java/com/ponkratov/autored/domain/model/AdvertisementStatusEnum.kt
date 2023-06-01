package com.ponkratov.autored.domain.model

enum class AdvertisementStatusEnum(val id: Long, val desc: String) {
    STATUS_MODERATING(1, "На модерации"),
    STATUS_ACCEPTED(2, "Принято"),
    STATUS_REJECTED(3, "Отклонено");

    companion object {
        fun getDescriptionById(id: Long): String {
            AdvertisementStatusEnum.values().forEach {
                if (it.id == id) {
                    return it.desc
                }
            }

            return ""
        }
    }
}