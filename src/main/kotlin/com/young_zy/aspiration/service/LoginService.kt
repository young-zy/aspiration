package com.young_zy.aspiration.service

import com.young_zy.aspiration.model.Token
import com.young_zy.aspiration.model.UserEntity
import com.young_zy.aspiration.repo.UserNativeRepository
import com.young_zy.aspiration.service.exception.AuthException
import com.young_zy.aspiration.service.exception.NotAcceptableException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import kotlin.math.abs

@Service
class LoginService {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
    @Autowired
    private lateinit var tokenRedisTemplate: RedisTemplate<String, Token>

    @Autowired
    private lateinit var userNativeRepository: UserNativeRepository

    suspend fun login(username: String, password: String): String {
        val user: UserEntity = userNativeRepository.getUser(username)
                    ?: throw NotAcceptableException("username $username does not exist")
        logger.info("acquired user from database")
        return if (PasswordHash.validatePassword(password, user.hashedPassword)) {
            val random = SecureRandom()
            val bytes = ByteArray(20)
            random.nextBytes(bytes)
            val longToken = abs(random.nextLong())
            val tokenStr = longToken.toString(16)
            val token = Token(
                    "$username:$tokenStr",
                    user.uid,
                    user.username,
                    user.auth,
                    user.region
            )
            withContext(Dispatchers.IO){
                tokenRedisTemplate.opsForValue().set(token.token, token, 1, TimeUnit.DAYS)
                logger.info("token of user ${token.username} saved successfully")
            }
            logger.info("user ${token.username} logged in successfully")
            token.token
        } else {
            logger.info("user $username entered wrong password")
            throw NotAcceptableException("Password Incorrect")
        }
    }

    suspend fun logout(token: String){
        tokenRedisTemplate.delete(token)
    }

    @Throws(AuthException::class)
    fun hasAuth(tokenObj: Token?, roles: List<AuthEnum>) {
        tokenObj ?: throw AuthException("user not login or token has expired")
        if (AuthEnum.valueOf(tokenObj.auth) in roles) {
            return
        } else {
            logger.info("user ${tokenObj.username} failed to pass the auth check")
            throw AuthException("this operation is not allowed as role of ${tokenObj.auth}")
        }
    }

    @Throws(AuthException::class)
    fun getToken(token: String): Token {
        return tokenRedisTemplate.opsForValue().get(token) ?: throw AuthException("not logged in or token expired")
    }

}