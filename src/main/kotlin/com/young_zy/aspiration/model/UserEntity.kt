package com.young_zy.aspiration.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("user")
data class UserEntity(
        @Id
        var uid: Int? = null,
        var username: String,
        var realName: String,
        var hashedPassword: String,
        var auth: String,
        var region: String,
        var gender: String,
        var age: Int
)