package com.TelemondoActivity1.TelemondoActivity1.scheduler

import com.TelemondoActivity1.TelemondoActivity1.controller.ClassmateController
import com.TelemondoActivity1.TelemondoActivity1.service.ClassmateService
import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ClassmateUpdateJob(private val classmateService: ClassmateService) : QuartzJobBean() {
    override fun executeInternal(context: JobExecutionContext) {
        val dataMap = context.mergedJobDataMap
        val id = UUID.fromString(dataMap.getString("id"))
        val name = dataMap.getString("name")
        val ageString = dataMap.getString("age")

        val age: Int? = ageString?.toIntOrNull()

        val dto = ClassmateController.ClassmateUpdateDTO(
            name = name,
            age = age
        )

        classmateService.update(id,dto)
    }
}