package com.young_zy.aspiration.controller

import com.young_zy.aspiration.service.PlanService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@Controller
class PlanController {

    @Autowired
    private lateinit var planService: PlanService

    @PutMapping("/plan")
    suspend fun updatePlan(@RequestHeader token: String, @RequestParam("file") file: MultipartFile) {
        return planService.updatePlan(token, file)
    }
}