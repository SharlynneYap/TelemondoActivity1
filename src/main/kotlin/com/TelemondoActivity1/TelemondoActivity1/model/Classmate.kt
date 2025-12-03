package com.TelemondoActivity1.TelemondoActivity1.model
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.Type
import java.sql.Types
import java.util.UUID

@Entity
@Table(name = "classmates")
open class Classmate{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    @Column(columnDefinition = "CHAR(36)", nullable = false, unique = true)
    var id: UUID? = null

    lateinit var name: String
    var age: Int? = null
}

