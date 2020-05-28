package com.young_zy.aspiration.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("plan")
data class PlanEntity(
        @Id
        var planId: Int? = null,
        var school: String,
        var major: String,
        var region: String,
        var amount: Int
)