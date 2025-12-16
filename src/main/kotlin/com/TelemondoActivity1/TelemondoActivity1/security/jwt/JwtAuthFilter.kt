package com.TelemondoActivity1.TelemondoActivity1.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    // Reads request cookies; looks for jwt
    private fun getJwtFromCookies(request: HttpServletRequest): String? {
        val cookies = request.cookies ?: return null
        return cookies.firstOrNull { it.name == "jwt" }?.value
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
//        //for debugging
//        println("REQ ${request.method} ${request.requestURI}")
//        println("Cookie header: ${request.getHeader("Cookie")}")
//        println("Cookies parsed: ${request.cookies?.map { it.name } }")

        // stops if authentication is already set for this request
        if (SecurityContextHolder.getContext().authentication != null) {
            filterChain.doFilter(request, response)
            return
        }

        val token = getJwtFromCookies(request)

        //check if token is null
        if (token.isNullOrBlank()) {
            filterChain.doFilter(request, response)
            return
        }

        //
        val decoded = jwtService.validateAndDecode(token)
        if (decoded != null) {
            val username = decoded.subject

            // roles claim must contain values like: ROLE_USER, ROLE_ADMIN
            val roles = decoded.getClaim("roles")
                ?.asList(String::class.java)
                ?: emptyList()

            val authorities = roles.map { SimpleGrantedAuthority(it) }

            val auth = UsernamePasswordAuthenticationToken(
                username,
                null,
                authorities
            ).apply {
                details = WebAuthenticationDetailsSource().buildDetails(request)
            }

            SecurityContextHolder.getContext().authentication = auth
        }
//        println("SecurityContext auth now: ${SecurityContextHolder.getContext().authentication}")
        filterChain.doFilter(request, response)
    }
}
