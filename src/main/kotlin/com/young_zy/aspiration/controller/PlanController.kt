package com.young_zy.aspiration.controller

import com.young_zy.aspiration.service.PlanService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController

@RestController
class PlanController {

    @Autowired
    private lateinit var planService: PlanService

    @PutMapping("/plan")
    suspend fun updatePlan(@RequestHeader token: String, @RequestPart file: FilePart) {
        planService.updatePlan(token, file)
    }
}