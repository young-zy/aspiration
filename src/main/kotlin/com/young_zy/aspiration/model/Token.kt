package com.young_zy.aspiration.model

data class Token(
        var token: String = "",
        val uid: Int = -1,
        var username: String = "",
        var auth: String = "",
        var region: String = ""
)