package com.example.represponsa.presentation.ui.registerUser

import com.example.represponsa.data.model.RolesEnum

data class RegisterState(
    val userName: String = "",
    val userNameError: String? = null,
    val nickname: String = "",
    val nicknameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val phone: String = "",
    val phoneError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPwd: String = "",
    val confirmPwdError: String? = null,
    val hasTypedConfirmPwd: Boolean = false,
    val selectedRoles: List<RolesEnum> = emptyList(),
    val republic: String = "",
    val isLoading:  Boolean = false
)