package com.example.represponsa.data.model

data class Republic(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val petCount: Int = 0,
    val residentCount: Int = 0,
    val roles: List<String> = emptyList(),
    val billsDueDay: Int = 1,
    val rentDueDay: Int = 1
)
