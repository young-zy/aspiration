package com.young_zy.aspiration.service

import com.young_zy.aspiration.repo.UserNativeRepository
import com.young_zy.aspiration.service.exception.AuthException
import com.young_zy.aspiration.service.exception.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {
    @Autowired private lateinit var userNativeRepository: UserNativeRepository

    @Autowired private lateinit var loginService: LoginService

    @Throws(AuthException::class)
    suspend fun getScore(token: String): MutableMap<String, Any> {
        val tokenObj = loginService.getToken(token)
        loginService.hasAuth(tokenObj, listOf(AuthEnum.STUDENT))
        return userNativeRepository.getScore(tokenObj.uid) ?: throw NotFoundException("score for uid ${tokenObj.uid} not found")
    }

    suspend fun updateUserInfo(token: String){
        val tokenObj = loginService.getToken(token)
    }

}