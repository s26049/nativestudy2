package com.teamnative.nativestudy.global.config

import com.teamnative.nativestudy.domain.auth.service.AccessTokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class BearerTokenAuthenticationFilter(
    private val accessTokenService: AccessTokenService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authorization = request.getHeader("Authorization")
        if (authorization?.startsWith("Bearer ", ignoreCase = true) == true) {
            val username = accessTokenService.username(authorization.substringAfter(' '))
            if (username != null) {
                SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    AuthorityUtils.NO_AUTHORITIES,
                )
            }
        }
        filterChain.doFilter(request, response)
    }
}
