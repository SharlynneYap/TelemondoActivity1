package com.TelemondoActivity1.TelemondoActivity1.scheduler

import com.TelemondoActivity1.TelemondoActivity1.controller.ClassmateController
import com.TelemondoActivity1.TelemondoActivity1.service.ClassmateService
import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component

@Component
class ClassmateCreateJob(private val classmateService: ClassmateService) : QuartzJobBean() {

    override fun executeInternal(context: JobExecutionContext) {
        val dataMap = context.mergedJobDataMap

        val name = dataMap.getString("name")
        val ageString = dataMap.getString("age")

        val age: Int? = ageString?.toIntOrNull()

        val dto = ClassmateController.ClassmateCreateDTO(
            name = name,
            age = age
        )

        classmateService.add(dto)
    }
}
