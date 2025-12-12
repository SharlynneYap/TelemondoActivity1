package com.TelemondoActivity1.TelemondoActivity1.security.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
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
        //for debugging
        println("REQ ${request.method} ${request.requestURI}")
        println("Cookie header: ${request.getHeader("Cookie")}")
        println("Cookies parsed: ${request.cookies?.map { it.name } }")

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

        //validate user and gets username
        val username = jwtService.validateAndGetUsername(token)
        println("Username $username")
        if (username != null) {
            val userDetails = userDetailsService.loadUserByUsername(username)

            //Create an authentication object for Spring Security
            val auth = UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.authorities
            ).apply {
                details = WebAuthenticationDetailsSource().buildDetails(request)
            }

            // Store authentication in SecurityContext
            SecurityContextHolder.getContext().authentication = auth
        }
        println("SecurityContext auth now: ${SecurityContextHolder.getContext().authentication}")
        filterChain.doFilter(request, response)
    }
}
