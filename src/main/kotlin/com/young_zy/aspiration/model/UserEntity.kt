package com.young_zy.aspiration.model

data class UserEntity(
    var uid: Int,
    var username: String,
    var realName: String,
    var hashedPassword: String,
    var auth: String,
    var region: String,
    var gender: String,
    var age: Int
)