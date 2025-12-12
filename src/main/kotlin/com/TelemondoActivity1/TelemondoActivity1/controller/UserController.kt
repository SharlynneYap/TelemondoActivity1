    package com.TelemondoActivity1.TelemondoActivity1.controller

    import com.TelemondoActivity1.TelemondoActivity1.model.ERole
    import com.TelemondoActivity1.TelemondoActivity1.model.User
    import com.TelemondoActivity1.TelemondoActivity1.service.UserService
    import jakarta.servlet.http.HttpServletResponse
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
    class UserController(
        private val service: UserService,
    ) {

        @PreAuthorize("hasRole('ADMIN')")
        @GetMapping("/users")
        fun getUser(): ResponseEntity<List<User>> =
            runCatching {
                val users = service.getAll()
                ResponseEntity.ok(users)
            }.getOrElse {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
            }

        data class UserCreateDTO(
            val username: String,
            val email: String,
            val password: String
        )

        @PostMapping("/auth/sign-up")
        fun createUser(@RequestBody dto: UserCreateDTO): ResponseEntity<User> =
            runCatching{
                val saved = service.add(dto)
                ResponseEntity.status(HttpStatus.CREATED).body(saved)
            }.getOrElse{
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
            }

        data class UserUpdateDTO(
            val username: String? = null,
            val email: String? = null,
            val password: String? = null
        )

        @PreAuthorize("hasRole('ADMIN')")
        @PutMapping("/auth/{id}")
        fun updateUser(
            @PathVariable id: UUID,
            @RequestBody newData: UserUpdateDTO
        ): ResponseEntity<User> =
            runCatching {
                val updated = service.update(id, newData)
                ResponseEntity.ok(updated)
            }.getOrElse {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
            }

        @PreAuthorize("hasRole('ADMIN')")
        @DeleteMapping("/auth/{id}")
        fun deleteUser(@PathVariable id: UUID): ResponseEntity<Void> {
            service.delete(id)
            return ResponseEntity.noContent().build()
        }

        data class LoginRequestDTO(
            val username: String,
            val password: String,
        )

        data class LoginResponseDTO(
            val message: String,
            val username: String,
            val roles: List<String>,
        )

        @PostMapping("/auth/log-in")
        fun login(
            @RequestBody dto: LoginRequestDTO,
            response: HttpServletResponse
        ): ResponseEntity<LoginResponseDTO> =
            runCatching {
                val result = service.login(dto, response)
                ResponseEntity.ok(result)
            }.getOrElse {
                // wrong username/password
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }

        @PostMapping("/auth/log-out")
        fun logout(response: HttpServletResponse): ResponseEntity<Void> {
            return try {
                service.logout(response)
                ResponseEntity.noContent().build()
            } catch (e: Exception) {
                ResponseEntity.status(500).build()
            }
        }

        data class UpdateUserRolesDTO(
            val roles: Set<ERole>
        )

        @PreAuthorize("hasRole('ADMIN')")
        @PutMapping("/users/{id}/roles")
        fun updateUserRoles(
            @PathVariable id: UUID,
            @RequestBody dto: UpdateUserRolesDTO
        ): ResponseEntity<User> =
            runCatching {
                val updated = service.updateRoles(id, dto.roles)
                ResponseEntity.ok(updated)
            }.getOrElse {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
            }
    }