package com.TelemondoActivity1.TelemondoActivity1.service

import com.TelemondoActivity1.TelemondoActivity1.controller.ClassmateController
import com.TelemondoActivity1.TelemondoActivity1.mapper.ClassmateMapper
import com.TelemondoActivity1.TelemondoActivity1.model.Classmate
import com.TelemondoActivity1.TelemondoActivity1.repo.ClassmateRepo
import com.TelemondoActivity1.TelemondoActivity1.scheduler.ClassmateCreateJob
import com.TelemondoActivity1.TelemondoActivity1.scheduler.ClassmateDeleteJob
import com.TelemondoActivity1.TelemondoActivity1.scheduler.ClassmateNotificationJob
import com.TelemondoActivity1.TelemondoActivity1.scheduler.ClassmateUpdateJob
import jakarta.transaction.Transactional
import org.quartz.Scheduler
import org.springframework.stereotype.Service
import java.util.UUID
import org.quartz.JobBuilder
import org.quartz.JobKey
import org.quartz.SimpleScheduleBuilder
import org.quartz.TriggerBuilder
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

@Service
class ClassmateService (
    private val repo: ClassmateRepo,
    private val mapper: ClassmateMapper,
    private val scheduler: Scheduler
){
    fun getAll(): List<Classmate> = runCatching {
            repo.findAll()
        }.getOrThrow()

    @Transactional
    fun add(dto: ClassmateController.ClassmateCreateDTO): Classmate = runCatching {
        val entity = mapper.toEntity(dto)
        repo.save(entity)
    }.getOrThrow()

    @Transactional
    fun update(id: UUID, dto: ClassmateController.ClassmateUpdateDTO): Classmate = runCatching {
            val existing = repo.findById(id).orElseThrow { IllegalArgumentException("Classmate not found") }

            mapper.updateEntityFromDto(dto, existing)
            repo.save(existing)
        }.getOrThrow()

    @Transactional
    fun delete(id: UUID){
        repo.deleteById(id)
    }

    @Transactional
    fun addRecurring(intervalInMinutes: Int): Date = runCatching {
            val jobName = "classmate-notification-job"
            val jobGroup = "CLASSMATE-NOTIFICATION"

            val jobDetail = JobBuilder.newJob(ClassmateNotificationJob::class.java)
                .withIdentity(jobName, jobGroup)
                .build()

            val trigger = TriggerBuilder.newTrigger()
                .withIdentity("${jobName}-trigger", jobGroup)
                .forJob(jobDetail)
                .withSchedule(
                    SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMinutes(intervalInMinutes)
                        .repeatForever()
                )
                .startNow()
                .build()

            scheduler.scheduleJob(jobDetail, trigger)
        }.getOrThrow()

    @Transactional
    fun scheduleCreate(dto: ClassmateController.ClassmateCreateScheduleDto): String = runCatching {
        val jobId = UUID.randomUUID().toString()
        val jobName = "create-$jobId"
        val jobGroup = "CLASSMATE-CREATE-SCHEDULED"
        val jobKey = JobKey(jobName, jobGroup)

        val jobDetail = JobBuilder.newJob(ClassmateCreateJob::class.java)
            .withIdentity(jobKey)
            .usingJobData("name", dto.name)
            .usingJobData("age", dto.age?.toString() ?: "")
            .build()

        val startTime = Date.from(
            Instant.now().plus(dto.delayInMinutes.toLong(), ChronoUnit.MINUTES)
        )

        val trigger = TriggerBuilder.newTrigger()
            .withIdentity("$jobName-trigger", jobGroup)
            .forJob(jobDetail)
            .startAt(startTime)
            .build()

        scheduler.scheduleJob(jobDetail, trigger)
        "Created a schedule for creating classmate record: $jobId"
    }.getOrThrow()

    @Transactional
    fun scheduleUpdate(dto: ClassmateController.ClassmateUpdateScheduleDto): String = runCatching{
        val jobId = UUID.randomUUID().toString()
        val jobName = "update-$jobId"
        val jobGroup = "CLASSMATE-UPDATE-SCHEDULED"
        val jobKey = JobKey(jobName, jobGroup)

        val jobDetail = JobBuilder.newJob(ClassmateUpdateJob::class.java)
            .withIdentity(jobKey)
            .usingJobData("id", dto.id.toString())
            .usingJobData("name", dto.name)
            .usingJobData("age", dto.age?.toString() ?: "")
            .build()

        val startTime = Date.from(
            Instant.now().plus(dto.delayInMinutes.toLong(), ChronoUnit.MINUTES)
        )

        val trigger = TriggerBuilder.newTrigger()
            .withIdentity("$jobName-trigger", jobGroup)
            .forJob(jobDetail)
            .startAt(startTime)
            .build()

        scheduler.scheduleJob(jobDetail, trigger)
        "Created a schedule for updating classmate record: $jobId"
    }.getOrThrow()

    @Transactional
    fun scheduleDelete(dto: ClassmateController.ClassmateDeleteScheduleDto): String = runCatching {
        val jobId = UUID.randomUUID().toString()
        val jobName = "delete-$jobId"
        val jobGroup = "CLASSMATE-DELETE-SCHEDULED"
        val jobKey = JobKey(jobName, jobGroup)

        val jobDetail = JobBuilder.newJob(ClassmateDeleteJob::class.java)
            .withIdentity(jobKey)
            .usingJobData("id", dto.id.toString())
            .build()

        val startTime = Date.from(
            Instant.now().plus(dto.delayInMinutes.toLong(), ChronoUnit.MINUTES)
        )

        val trigger = TriggerBuilder.newTrigger()
            .withIdentity("$jobName-trigger", jobGroup)
            .forJob(jobDetail)
            .startAt(startTime)
            .build()

        scheduler.scheduleJob(jobDetail, trigger)
        "Created a schedule for delete record: $jobId"
    }.getOrThrow()
}