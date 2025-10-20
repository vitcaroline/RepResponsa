package com.example.represponsa.data.model

data class User(
    val uid: String = "",
    val userName: String = "",
    val nickName: String = "",
    val email: String = "",
    val phone: String = "",
    val republicName: String = "",
    val republicId: String = "",
    val role: String = "",
    val monthlyPoints: Int = 0,
    val lastPointsResetMonth: Int = -1
)
