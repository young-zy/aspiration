package com.young_zy.aspiration.repo

import kotlinx.coroutines.reactive.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.r2dbc.core.*
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.stereotype.Repository

@Repository
class AspirationNativeRepository {

    @Autowired private lateinit var r2dbcDatabaseClient: DatabaseClient

    suspend fun deleteAspirationByUid(uid: Int): Int {
//        return r2dbcDatabaseClient.execute("delete from aspiration where uid = :uid")
//                .bind("uid", uid)
//                .fetch()
//                .awaitRowsUpdated()
        return r2dbcDatabaseClient
                .delete()
                .from("aspiration")
                .matching(where("uid").`is`(uid))
                .fetch()
                .awaitRowsUpdated()
    }

    suspend fun countAspirationByUid(uid: Int): Int? {
        return r2dbcDatabaseClient.execute("select count(uid) as count from aspiration where uid = :uid")
                .bind("uid", uid)
                .map{
                    t -> t.get("count", Int::class.java)
                }
                .awaitOne()
    }

    suspend fun updateAspirationByUid(
            uid: Int,
            plan_id1: Int, adjust1: Boolean,
            plan_id2: Int, adjust2: Boolean,
            plan_id3: Int, adjust3: Boolean
    ): Void? {
        val sql = "replace into aspiration (uid, plan_id1, adjust1, plan_id2, adjust2, plan_id3, adjust3) VALUES (:uid, :plan_id1, :adjust1, :plan_id2, :adjust2, :plan_id3, :adjust3)"
        return r2dbcDatabaseClient.execute(sql)
                .bind("uid", uid)
                .bind("plan_id1", plan_id1)
                .bind("plan_id2", plan_id2)
                .bind("plan_id3", plan_id3)
                .bind("adjust1", adjust1)
                .bind("adjust2", adjust2)
                .bind("adjust3", adjust3)
                .then()
                .awaitSingle()
    }



}