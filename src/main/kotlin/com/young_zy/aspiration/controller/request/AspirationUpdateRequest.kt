package com.young_zy.aspiration.controller.request

data class AspirationUpdateRequest(
        var plan_id: Int,
        var adjust1: Boolean,
        var plan_id2: Int,
        var adjust2: Boolean,
        var plan_id3: Int,
        var adjust3: Boolean
)