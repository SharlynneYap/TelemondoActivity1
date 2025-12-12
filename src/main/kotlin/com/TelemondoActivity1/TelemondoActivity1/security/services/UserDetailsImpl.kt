package com.bezkoder.springjwt.security.services

import com.TelemondoActivity1.TelemondoActivity1.model.User
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

class UserDetailsImpl(
    private val id: UUID?,                           // change to UUID? if needed
    private val usernameValue: String,
    private val emailValue: String,
    @field:JsonIgnore
    private val passwordValue: String,
    private val authoritiesValue: Collection<out GrantedAuthority>
) : UserDetails {

    companion object {
        private const val serialVersionUID: Long = 1L

        fun build(user: User): UserDetailsImpl {
            val authorities: List<GrantedAuthority> = user.roles
                .map { role -> SimpleGrantedAuthority(role.name.name) }

            return UserDetailsImpl(
                user.id,                 // if UUID: user.id!!
                user.username,
                user.email,
                user.password,
                authorities
            )
        }
    }

    override fun getAuthorities(): Collection<out GrantedAuthority> = authoritiesValue

    fun getId(): UUID? = id                      // if UUID: UUID
    fun getEmail(): String = emailValue

    override fun getPassword(): String = passwordValue

    override fun getUsername(): String = usernameValue

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as UserDetailsImpl
        return id == other.id
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0
}
