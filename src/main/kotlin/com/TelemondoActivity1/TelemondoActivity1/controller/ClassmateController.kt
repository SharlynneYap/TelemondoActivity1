package com.TelemondoActivity1.TelemondoActivity1.controller

import com.TelemondoActivity1.TelemondoActivity1.mapper.ClassmateMapper
import com.TelemondoActivity1.TelemondoActivity1.model.Classmate
import com.TelemondoActivity1.TelemondoActivity1.service.ClassmateService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.UUID
import org.quartz.Scheduler
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping("/api")
class ClassmateController(
    private val service: ClassmateService,
){
    data class ClassmateResponseDTO(
        val id: UUID,
        val name: String,
        val age: Int?
    )

    @GetMapping("/classmates")
    fun getAllClassmates(): ResponseEntity<List<Classmate>> =
        runCatching {
            val classmates = service.getAll()
            ResponseEntity.ok(classmates)
        }.getOrElse {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }

    data class ClassmateCreateDTO(
        val name: String,
        val age: Int?
    )

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/classmates")
    fun addClassmate(@RequestBody classmateCreateDTO: ClassmateCreateDTO)
            : ResponseEntity<Classmate> =
        runCatching {
            val saved = service.add(classmateCreateDTO)
            ResponseEntity.status(HttpStatus.CREATED).body(saved)
        }.getOrElse {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }

    data class ClassmateUpdateDTO(
        val name: String,
        val age: Int?
    )

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/classmates/{id}")
    fun updateClassmate(
        @PathVariable id: UUID,
        @RequestBody newData: ClassmateUpdateDTO
    ): ResponseEntity<Classmate> =
        runCatching {
            val updated = service.update(id, newData)
            ResponseEntity.ok(updated)
        }.getOrElse {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/classmates/{id}")
    fun deleteClassmate(@PathVariable id: UUID): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/classmates/schedule-notification")
    fun addRecurring(@RequestParam intervalInMinutes: Int) : ResponseEntity<String> = runCatching  {
        val result = service.addRecurring(intervalInMinutes)
        ResponseEntity.ok(result.toString())
    }.getOrElse{
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
    }

    data class ClassmateCreateScheduleDto(
        val name: String,
        val age: Int?,
        val delayInMinutes: Int
    )

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/classmates/schedule-create")
    fun scheduleCreate(@RequestBody dto: ClassmateCreateScheduleDto): ResponseEntity<String> = runCatching {
        val result = service.scheduleCreate(dto)
        ResponseEntity.status(HttpStatus.ACCEPTED).body(result)
    }.getOrElse {
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
    }

    data class ClassmateUpdateScheduleDto(
        val id: UUID,
        val name: String,
        val age: Int?,
        val delayInMinutes: Int
    )

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/classmates/schedule-update")
    fun scheduleUpdate(@RequestBody dto: ClassmateUpdateScheduleDto): ResponseEntity<String> = runCatching {
        val result = service.scheduleUpdate(dto)
        ResponseEntity.status(HttpStatus.ACCEPTED).body(result)
    }.getOrElse {
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
    }

    data class ClassmateDeleteScheduleDto(
        val id: UUID,
        val delayInMinutes: Int
    )

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/classmates/schedule-delete")
    fun scheduleDelete(@RequestBody dto: ClassmateDeleteScheduleDto): ResponseEntity<String> = runCatching {
        val result = service.scheduleDelete(dto)
        ResponseEntity.status(HttpStatus.ACCEPTED).body(result.toString())
    }.getOrElse {
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
    }

}
