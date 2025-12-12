package com.TelemondoActivity1.TelemondoActivity1.controller

import com.TelemondoActivity1.TelemondoActivity1.controller.ClassmateController.ClassmateCreateDTO
import com.TelemondoActivity1.TelemondoActivity1.controller.ClassmateController.ClassmateUpdateDTO
import com.TelemondoActivity1.TelemondoActivity1.model.Classmate
import com.TelemondoActivity1.TelemondoActivity1.model.ERole
import com.TelemondoActivity1.TelemondoActivity1.model.Role
import com.TelemondoActivity1.TelemondoActivity1.model.User
import com.TelemondoActivity1.TelemondoActivity1.service.RoleService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api")
class RoleController (
    private val service: RoleService,
){
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/role")
    fun getRole(): ResponseEntity<List<Role>> =
        runCatching {
            val users = service.getAll()
            ResponseEntity.ok(users)
        }.getOrElse {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }

    data class RoleCreateDTO(
        val name: ERole,
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/role")
    fun addRole(@RequestBody roleCreateDTO: RoleCreateDTO)
            : ResponseEntity<Role> =
        runCatching {
            val saved = service.add(roleCreateDTO)
            ResponseEntity.status(HttpStatus.CREATED).body(saved)
        }.getOrElse {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }

    data class RoleUpdateDTO(
        val name: ERole,
    )

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/role/{id}")
    fun updateRole(
        @PathVariable id: UUID,
        @RequestBody newData: RoleUpdateDTO
    ): ResponseEntity<Role> =
        runCatching {
            val updated = service.update(id, newData)
            ResponseEntity.ok(updated)
        }.getOrElse {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/role/{id}")
    fun deleteClassmate(@PathVariable id: UUID): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }

}