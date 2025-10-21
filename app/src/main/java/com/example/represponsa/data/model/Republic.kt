package com.example.represponsa.data.model

data class RentPaymentConfig(
    val day: Int = 1,
    val billsDueDay: Int = 1,
    val isFixed: Boolean = true
)
data class Republic(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val petCount: Int = 0,
    val residentCount: Int = 0,
    val roles: List<String> = emptyList(),
    val rentPaymentConfig: RentPaymentConfig? = null
)
