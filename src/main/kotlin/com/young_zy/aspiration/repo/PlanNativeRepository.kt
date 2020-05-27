package com.young_zy.aspiration.repo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.awaitRowsUpdated
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.stereotype.Repository

@Repository
class PlanNativeRepository {

    @Autowired lateinit var r2dbcDatabaseClient: DatabaseClient

    suspend fun deleteBySchool(school: String): Int {
        return r2dbcDatabaseClient
                .delete()
                .from("plan")
                .matching(where("school").`is`(school))
                .fetch()
                .awaitRowsUpdated()
    }
}