package com.young_zy.aspiration.service

import com.young_zy.aspiration.repo.AspirationNativeRepository
import com.young_zy.aspiration.service.exception.AuthException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AspirationService {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @Autowired private lateinit var loginService: LoginService

    @Autowired private lateinit var aspirationNativeRepository: AspirationNativeRepository

    @Throws(AuthException::class)
    suspend fun updateAspiration(token: String, plan_id1: Int, adjust1: Boolean, plan_id2: Int, adjust2: Boolean, plan_id3: Int, adjust3: Boolean){
        val tokenObj = loginService.getToken(token) ?: throw AuthException("not logged in")
        loginService.hasAuth(tokenObj, listOf(AuthEnum.STUDENT))
        aspirationNativeRepository.updateAspirationByUid(
                tokenObj.uid,
                plan_id1, adjust1,
                plan_id2, adjust2,
                plan_id3, adjust3
        )
        logger.info("user ${tokenObj.username} updated the aspiration")
    }
}