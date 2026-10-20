package com.teamnative.nativestudy.domain.auth.controller

import com.teamnative.nativestudy.domain.auth.dto.LoginRequest
import com.teamnative.nativestudy.domain.auth.dto.LoginResponse
import com.teamnative.nativestudy.domain.auth.dto.SignupRequest
import com.teamnative.nativestudy.domain.auth.dto.SignupResponse
import com.teamnative.nativestudy.domain.auth.service.AuthService
import com.teamnative.nativestudy.global.exception.UnauthorizedException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.DeleteMapping

@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {

    @Operation(summary = "회원가입", description = "ID와 비밀번호를 입력해 회원가입합니다.")
    @PostMapping("/signup")
    fun signup(@Valid @RequestBody request: SignupRequest): SignupResponse =
        authService.signup(request)

    @Operation(summary = "로그인", description = "아이디/비밀번호로 로그인하여 액세스 토큰을 발급받습니다.")
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): LoginResponse =
        authService.login(request)

    @Operation(summary = "로그아웃", description = "현재 Bearer 액세스 토큰을 무효화합니다.")
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(@RequestHeader("Authorization") authorization: String) {
        val token = authorization.takeIf { it.startsWith("Bearer ", ignoreCase = true) }
            ?.substringAfter(' ')
            ?.takeIf { it.isNotBlank() }
            ?: throw UnauthorizedException("Bearer token is required.")
        authService.logout(token)
    }

    @Operation(summary = "회원 탈퇴", description = "인증된 사용자의 계정을 삭제합니다.")
    @DeleteMapping("/delete-account")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAccount(request: HttpServletRequest) {
        val username = request.userPrincipal?.name
            ?: throw UnauthorizedException("Authentication is required.")
        val token = request.getHeader("Authorization")
            ?.takeIf { it.startsWith("Bearer ", ignoreCase = true) }
            ?.substringAfter(' ')
        authService.deleteAccount(username, token)
    }
}
