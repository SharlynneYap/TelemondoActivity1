package com.TelemondoActivity1.TelemondoActivity1.repo

import com.TelemondoActivity1.TelemondoActivity1.model.ERole
import com.TelemondoActivity1.TelemondoActivity1.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface  RoleRepo: JpaRepository<Role, UUID> {
    fun findByName(name: ERole): Role?
    fun findByNameIn(names: Collection<ERole>): List<Role>
}