package com.young_zy.aspiration.controller

data class Response<T> (
        var code: Int = 200,
        var success: Boolean = true,
        var message: String = "",
        var data: T? = null,
        var timestamp: Long = System.currentTimeMillis()
)