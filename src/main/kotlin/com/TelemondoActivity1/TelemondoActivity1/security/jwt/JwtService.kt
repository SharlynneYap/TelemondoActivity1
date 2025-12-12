package com.TelemondoActivity1.TelemondoActivity1.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtService(
    @Value("\${app.jwt.secret}") private val jwtSecret: String,
    @Value("\${app.jwt.expiration-ms}") private val jwtExpirationMs: Long,
) {

    private fun algorithm(): Algorithm = Algorithm.HMAC256(jwtSecret)

    //usage:
    //to generate token upon login
    //val token = jwtService.generateToken(userDetails.username, roles)
    fun generateToken(username: String, roles: Collection<String>): String {
        val now = Date()
        val expiry = Date(now.time + jwtExpirationMs)

        return JWT.create()
            .withSubject(username)
            .withIssuedAt(now)
            .withExpiresAt(expiry)
            .withClaim("roles", roles.toList())
            .sign(algorithm())
    }

    //to validate user (used in JwtAuthFilter)
    fun validateAndGetUsername(token: String): String? =
        try {
            val verifier = JWT.require(algorithm()).build()
            val decoded: DecodedJWT = verifier.verify(token)
            decoded.subject
        } catch (ex: Exception) {
            println("JWT validation failed: ${ex.message}")
            null
        }
}
