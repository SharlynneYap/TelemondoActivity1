package com.TelemondoActivity1.TelemondoActivity1.service

import com.TelemondoActivity1.TelemondoActivity1.controller.ClassmateController
import com.TelemondoActivity1.TelemondoActivity1.mapper.ClassmateMapper
import com.TelemondoActivity1.TelemondoActivity1.model.Classmate
import com.TelemondoActivity1.TelemondoActivity1.repo.ClassmateRepo
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ClassmateService (
    private val repo: ClassmateRepo,
    private val mapper: ClassmateMapper
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

    fun delete(id: UUID){
        repo.deleteById(id)
    }
}