package com.TelemondoActivity1.TelemondoActivity1.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import java.sql.Types
import java.util.UUID

@Entity
@Table(name = "roles")
open class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    @Column(columnDefinition = "CHAR(36)", nullable = false, unique = true)
    open var id: UUID? = null

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false, unique = true)
    open lateinit var name: ERole
}