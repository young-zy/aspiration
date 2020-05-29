package com.young_zy.aspiration.service

import com.young_zy.aspiration.model.PlanEntity
import com.young_zy.aspiration.repo.PlanNativeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait
import java.io.File

@Service
class PlanService {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @Autowired
    private lateinit var planNativeRepository: PlanNativeRepository

    @Autowired
    private lateinit var transactionalOperator: TransactionalOperator

    @Autowired
    private lateinit var loginService: LoginService

    suspend fun updatePlan(token: String, planFile: FilePart) {
        val tokenObj = loginService.getToken(token)
        loginService.hasAuth(tokenObj, listOf(AuthEnum.SCHOOL_ADMIN))
        withContext(Dispatchers.IO) {
            val file: File = File.createTempFile("temp", null)
            planFile.transferTo(file)
            val inputStream = file.inputStream()
            val workBook = WorkbookFactory.create(inputStream)
            val sheet = workBook.getSheetAt(0)
            val school: String = sheet.getRow(1).getCell(0).stringCellValue
            transactionalOperator.executeAndAwait {
                planNativeRepository.deleteBySchool(school)     //删除原来所有该学校的记录
                logger.info("$school 原记录已删除")
                sheet.rowIterator().forEach {
                    if (it.rowNum == 0) {
                        return@forEach
                    }
                    val plan = PlanEntity(
                            school = it.getCell(0).stringCellValue,
                            major = it.getCell(1).stringCellValue,
                            region = it.getCell(2).stringCellValue,
                            amount = it.getCell(3).numericCellValue.toInt()
                    )
                    planNativeRepository.insertPlan(plan)
                }
                logger.info("$school 新纪录已保存")
                file.delete()
            }
        }
    }
}