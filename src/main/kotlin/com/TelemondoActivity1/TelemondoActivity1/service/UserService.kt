package com.TelemondoActivity1.TelemondoActivity1.service

import com.TelemondoActivity1.TelemondoActivity1.controller.ClassmateController
import com.TelemondoActivity1.TelemondoActivity1.controller.UserController
import com.TelemondoActivity1.TelemondoActivity1.mapper.UserMapper
import com.TelemondoActivity1.TelemondoActivity1.model.Classmate
import com.TelemondoActivity1.TelemondoActivity1.model.User
import com.TelemondoActivity1.TelemondoActivity1.model.ERole
import com.TelemondoActivity1.TelemondoActivity1.repo.RoleRepo
import com.TelemondoActivity1.TelemondoActivity1.repo.UserRepo
import com.TelemondoActivity1.TelemondoActivity1.security.jwt.JwtService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

@Service
class UserService(
    private val userRepo: UserRepo,
    private val roleRepo: RoleRepo,
    private val mapper: UserMapper,
    private val encoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    @Value("\${app.jwt.expiration-ms}") private val jwtExpirationMs: Long

    ) {
    @Transactional
    fun getAll(): List<User> = runCatching {
        userRepo.findAll()
    }.getOrThrow()

    @Transactional
    fun add(dto: UserController.UserCreateDTO): User = runCatching {

        //validation
        if (userRepo.existsByUsername(dto.username)) {
            throw IllegalArgumentException("Username already exists")
        }

        if (userRepo.existsByEmail(dto.email)) {
            throw IllegalArgumentException("Email already exists")
        }

        //crpyt password
        val entity = mapper.toEntity(dto)

        entity.password = encoder.encode(dto.password)!!
        userRepo.save(entity)

        // set user to the default role (ROLE_USER)
        val userRole = roleRepo.findByName(ERole.ROLE_USER)
            ?: throw IllegalStateException("ROLE_USER not found")

        entity.roles = mutableSetOf(userRole)

        //save
        userRepo.save(entity)
    }.getOrThrow()

    @Transactional
    fun update(id: UUID, dto: UserController.UserUpdateDTO): User = runCatching {
        val existing = userRepo.findById(id).orElseThrow { IllegalArgumentException("Classmate not found") }

        //validation
        if (dto.username != null && dto.username != existing.username) {
            if (userRepo.existsByUsername(dto.username)) {
                throw IllegalArgumentException("Username already exists")
            }
        }

        if (dto.email != null && dto.email != existing.email) {
            if (userRepo.existsByEmail(dto.email)) {
                throw IllegalArgumentException("Email already exists")
            }
        }

        //encode password
        mapper.updateEntityFromDto(dto, existing)
        if (!dto.password.isNullOrBlank()) {
            existing.password = encoder.encode(dto.password)!!
        }

        userRepo.save(existing)
    }.getOrThrow()

    @Transactional
    fun delete(id: UUID){
        userRepo.deleteById(id)
    }

    @Transactional
    fun login(dto: UserController.LoginRequestDTO, response: HttpServletResponse): UserController.LoginResponseDTO=runCatching{
        //authenticate credentials
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(dto.username, dto.password)
        )
        SecurityContextHolder.getContext().authentication = authentication

        //extract role
        val userDetails = authentication.principal as UserDetails
        val roles = userDetails.authorities
            .mapNotNull { it.authority }

        // generate jwt token
        val token = jwtService.generateToken(userDetails.username, roles)

        // Put JWT into cookie
        val cookie = Cookie("jwt", token).apply {
            isHttpOnly = true
            path = "/"
            maxAge = (jwtExpirationMs / 1000).toInt()
        }
        response.addCookie(cookie)

        UserController.LoginResponseDTO(
            message = "Login successful",
            username = userDetails.username,
            roles = roles
        )
    }.getOrThrow()

    @Transactional
    fun logout(response: HttpServletResponse) = runCatching {
        // Delete jwt cookie
        val cookie = Cookie("jwt", "").apply {
            path = "/"
            maxAge = 0
            isHttpOnly = true
        }
        response.addCookie(cookie)

        SecurityContextHolder.clearContext()
    }.getOrThrow()

    @Transactional
    fun updateRoles(id: UUID, roles: Set<ERole>): User = runCatching {
        val user = userRepo.findById(id).orElseThrow { IllegalArgumentException("User not found") }
        val roleEntities = roleRepo.findByNameIn(roles)
        user.roles = roleEntities.toMutableSet()
        userRepo.save(user)
    }.getOrThrow()
}