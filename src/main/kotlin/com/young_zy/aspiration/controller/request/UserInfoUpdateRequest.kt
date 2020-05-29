package com.young_zy.aspiration.controller.request

data class UserInfoUpdateRequest(
        val password: String,
        val newPassword: String?,
        val newAge: Int?,
        val newGender: String?
)