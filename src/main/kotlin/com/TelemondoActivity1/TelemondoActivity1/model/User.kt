package com.TelemondoActivity1.TelemondoActivity1.model
import com.TelemondoActivity1.TelemondoActivity1.model.Role
import com.fasterxml.jackson.annotation.JsonIgnore

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.JdbcTypeCode
import java.sql.Types
import java.util.UUID

@Entity
@Table(name = "users",)
open class User{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    @Column(columnDefinition = "CHAR(36)", nullable = false, unique = true)
    open var id: UUID? = null

    @Column(nullable = false, unique = true, length = 50)
    open lateinit var username: String

    @Column(nullable = false, unique = true, length = 50)
    open lateinit var email: String

    @JsonIgnore //hides password
    @Column(nullable = false, length = 255)
    open lateinit var password: String

    @ManyToMany
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    open var roles: MutableSet<Role> = mutableSetOf()
}
