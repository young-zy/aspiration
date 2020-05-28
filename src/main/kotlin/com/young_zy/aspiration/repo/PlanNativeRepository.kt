package com.young_zy.aspiration.repo

import com.young_zy.aspiration.model.PlanEntity
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.stereotype.Repository

@Repository
class PlanNativeRepository {

    @Autowired
    private lateinit var r2dbcDatabaseClient: DatabaseClient

    suspend fun deleteBySchool(school: String): Void? {
        return r2dbcDatabaseClient
                .delete()
                .from("plan")
                .matching(where("school").`is`(school))
                .then()
                .awaitSingle()
    }

    suspend fun insertPlan(plan: PlanEntity): Void? {
        return r2dbcDatabaseClient
                .insert()
                .into(PlanEntity::class.java)
                .using(plan)
                .then()
                .awaitSingle()
    }
}