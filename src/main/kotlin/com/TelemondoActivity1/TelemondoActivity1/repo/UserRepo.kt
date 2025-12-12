package com.TelemondoActivity1.TelemondoActivity1.repo

import com.TelemondoActivity1.TelemondoActivity1.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface  UserRepo: JpaRepository<User, UUID> {
    fun existsByUsername(username: String?): Boolean
    fun existsByEmail(email: String?): Boolean
    fun findByUsername(username: String?): User
}