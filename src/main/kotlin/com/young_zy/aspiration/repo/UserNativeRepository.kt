package com.young_zy.aspiration.repo

import com.young_zy.aspiration.model.UserEntity
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.awaitOneOrNull
import org.springframework.stereotype.Repository

@Repository
class UserNativeRepository {

    @Autowired
    private lateinit var r2dbcDatabaseClient: DatabaseClient

    suspend fun getUser(username: String): UserEntity? {
        return r2dbcDatabaseClient.execute("select * from user where username = :username")
                .`as`(UserEntity::class.java)
                .bind("username", username)
                .fetch()
                .awaitOneOrNull()
    }

    suspend fun getUser(uid: Int): UserEntity? {
        return r2dbcDatabaseClient.execute("select * from user where username = :uid")
                .`as`(UserEntity::class.java)
                .bind("uid", uid)
                .fetch()
                .awaitOneOrNull()
    }

    suspend fun getScore(uid: Int): MutableMap<String, Any>? {
        return r2dbcDatabaseClient.execute(
                "select real_name, chinese_score, math_score, english_score, other_score, other_subject_name from user natural join score where uid = :uid"
        )
                .bind("uid", uid)
                .fetch()
                .awaitOneOrNull()
    }

    suspend fun updateUserInfo(user: UserEntity): Void? {
        return r2dbcDatabaseClient.update()
                .table(UserEntity::class.java)
                .using(user)
                .then()
                .awaitFirstOrNull()
    }

    suspend fun insertUser(user: UserEntity): Void? {
        return r2dbcDatabaseClient.insert()
                .into(UserEntity::class.java)
                .using(user)
                .then()
                .awaitFirstOrNull()
    }
}