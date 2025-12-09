package com.TelemondoActivity1.TelemondoActivity1.scheduler

import com.TelemondoActivity1.TelemondoActivity1.service.ClassmateService
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ClassmateNotificationJob(
    private val classmateService: ClassmateService
) : Job {

    private val log = LoggerFactory.getLogger(ClassmateNotificationJob::class.java)

    override fun execute(context: JobExecutionContext) {
        log.info("Time: {}", context.fireTime)

        val classmates = classmateService.getAll()
        log.info("There are {} classmates", classmates.size)
    }
}
