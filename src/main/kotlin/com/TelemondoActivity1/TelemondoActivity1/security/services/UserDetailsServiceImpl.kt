package com.TelemondoActivity1.TelemondoActivity1.security

import com.TelemondoActivity1.TelemondoActivity1.repo.UserRepo
import com.bezkoder.springjwt.security.services.UserDetailsImpl
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailsServiceImpl(
    private val userRepo: UserRepo
) : UserDetailsService {

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepo.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found with username: $username")

        return UserDetailsImpl.build(user)
    }
}
