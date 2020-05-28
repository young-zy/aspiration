package com.young_zy.aspiration.service

import com.young_zy.aspiration.repo.UserNativeRepository
import com.young_zy.aspiration.service.exception.AuthException
import com.young_zy.aspiration.service.exception.NotAcceptableException
import com.young_zy.aspiration.service.exception.NotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.reactive.TransactionalOperator

@Service
class UserService {
    @Autowired
    private lateinit var userNativeRepository: UserNativeRepository

    @Autowired
    private lateinit var loginService: LoginService

    @Autowired
    private lateinit var transactionalOperator: TransactionalOperator

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @Throws(AuthException::class)
    suspend fun getScore(token: String): MutableMap<String, Any> {
        val tokenObj = loginService.getToken(token)
        loginService.hasAuth(tokenObj, listOf(AuthEnum.STUDENT))
        return userNativeRepository.getScore(tokenObj.uid) ?: throw NotFoundException("score for uid ${tokenObj.uid} not found")
    }

    suspend fun updateUserInfo(token: String, password: String,
                               newPassword: String?,
                               newAge: Int?,
                               newGender: String?): Void? {
        val tokenObj = loginService.getToken(token)
        val user = userNativeRepository.getUser(tokenObj.uid)
                ?: throw NotFoundException("user with uid ${tokenObj.uid} not found")
        if (!PasswordHash.validatePassword(password, user.hashedPassword)) {
            logger.info("user ${tokenObj.username} entered wrong password")
            throw NotAcceptableException("password not correct")
        }
        //update if not null
        newPassword?.let { user.hashedPassword = PasswordHash.createHash(it) }
        newAge?.let { user.age = it }
        newGender?.let { user.gender = newGender }
        return userNativeRepository.updateUserInfo(user).also {
            logger.info("user ${user.username}'s user info has been saved")
        }
    }

}