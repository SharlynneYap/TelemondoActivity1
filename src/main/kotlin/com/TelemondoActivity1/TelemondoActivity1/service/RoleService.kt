package com.TelemondoActivity1.TelemondoActivity1.service

import com.TelemondoActivity1.TelemondoActivity1.controller.RoleController
import com.TelemondoActivity1.TelemondoActivity1.mapper.RoleMapper
import com.TelemondoActivity1.TelemondoActivity1.model.Role
import com.TelemondoActivity1.TelemondoActivity1.repo.RoleRepo
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class RoleService(
    private val roleRepo: RoleRepo,
    private val roleMapper: RoleMapper

){
    @Transactional
    fun getAll(): List<Role> = runCatching {
        roleRepo.findAll()
    }.getOrThrow()

    @Transactional
    fun add(dto: RoleController.RoleCreateDTO): Role = runCatching {
        // checks
        if (roleRepo.findByName(dto.name) != null) {
            throw IllegalArgumentException("Role already exists")
        }
        val entity = roleMapper.toEntity(dto)
        roleRepo.save(entity)
    }.getOrThrow()

    @Transactional
    fun update(id: UUID, dto: RoleController.RoleUpdateDTO): Role = runCatching {
        val existing = roleRepo.findById(id).orElseThrow { IllegalArgumentException("Classmate not found") }

        roleMapper.updateEntityFromDto(dto, existing)
        roleRepo.save(existing)
    }.getOrThrow()

    @Transactional
    fun delete(id: UUID){
        roleRepo.deleteById(id)
    }
}