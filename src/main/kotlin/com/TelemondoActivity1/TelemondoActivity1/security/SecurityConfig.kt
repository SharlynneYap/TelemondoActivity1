package com.TelemondoActivity1.TelemondoActivity1.security

import com.TelemondoActivity1.TelemondoActivity1.security.jwt.AuthEntryPointJwt
import com.TelemondoActivity1.TelemondoActivity1.security.jwt.JwtAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableMethodSecurity(prePostEnabled = true) // to enable @PreAuthorize
class SecurityConfig {

    //usage:
    //encode password upon sign up
    //encoder.encode(dto.password)!!
    @Bean
    fun passwordEncoder(): PasswordEncoder =
        BCryptPasswordEncoder()

    //usage:
    //to authenticate user when logging in, authenticates credentials (username and password)
    //authenticationManager.authenticate( UsernamePasswordAuthenticationToken(dto.username, dto.password)
    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager =
        authConfig.authenticationManager

    //runs every request
    //authenticate incoming requests
    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtAuthFilter: JwtAuthFilter,
        authEntryPointJwt: AuthEntryPointJwt
    ): SecurityFilterChain {
        http.csrf { it.disable() }
            //ueses authentrypointjwt's message when it throws 401 (appears when there is no JWT / invalid JWT / expired JWT)
            .exceptionHandling {
                it.authenticationEntryPoint(authEntryPointJwt)
            }

            //stateless
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }

            //make specified paths public
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        "/api/auth/sign-up",
                        "/api/auth/log-in"
                    ).permitAll()
                    .anyRequest().authenticated()
            }

            //run JwtAuthFilter before Spring checks authentication
            http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
//            .httpBasic(Customizer.withDefaults())//temporary
//            .formLogin(Customizer.withDefaults())//temporary
        return http.build()
    }
}