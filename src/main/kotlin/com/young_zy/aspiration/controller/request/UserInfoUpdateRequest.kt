package com.young_zy.aspiration.controller.request

data class UserInfoUpdateRequest(
        val age: Int,
        val password: String,
        val newPassword: String?,
        val newAge: Int?,
        val newGender: String?
)