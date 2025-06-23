package com.example.represponsa.presentation.ui.registerUser

data class RegisterState(
    val firstName: String = "",
    val firstNameError: String? = null,
    val lastName: String = "",
    val lastNameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val phone: String = "",
    val phoneError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPwd: String = "",
    val confirmPwdError: String? = null,
    val hasTypedConfirmPwd: Boolean = false,
    val role: String = "",
    val republic: String = "",
    val isLoading:  Boolean = false
)