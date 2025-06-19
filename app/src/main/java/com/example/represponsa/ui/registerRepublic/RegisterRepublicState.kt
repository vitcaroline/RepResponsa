package com.example.represponsa.ui.registerRepublic

import com.example.represponsa.model.RolesEnum

data class RegisterRepublicState(
    val name: String = "",
    val address: String = "",
    val petCount: String = "",
    val residentCount: String = "",
    val selectedRoles: List<RolesEnum> = emptyList(),
    val isLoading: Boolean = false,

    val nameError: String? = null,
    val addressError: String? = null,
    val petCountError: String? = null,
    val residentCountError: String? = null,
)