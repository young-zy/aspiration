package com.young_zy.aspiration.controller

import com.young_zy.aspiration.controller.request.LoginRequest
import com.young_zy.aspiration.service.LoginService
import com.young_zy.aspiration.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class UserController {

    @Autowired private lateinit var loginService: LoginService

    @Autowired private lateinit var userService: UserService

    @GetMapping("/score")
    suspend fun getScore(@RequestHeader token: String): MutableMap<String, Any> {
        return userService.getScore(token)
    }

    @GetMapping("/result")
    suspend fun getResult(){

    }

    @PostMapping("/user/login")
    suspend fun login(@RequestBody requestBody:LoginRequest): String {
        return loginService.login(requestBody.username, requestBody.password)
    }

    @PostMapping("/user/logout")
    suspend fun logout(@RequestHeader token: String){
        loginService.logout(token)
    }


}