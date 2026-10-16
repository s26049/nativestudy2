package com.teamnative.nativestudy.domain.auth.controller

import com.teamnative.nativestudy.domain.auth.dto.LoginRequest
import com.teamnative.nativestudy.domain.auth.dto.LoginResponse
import com.teamnative.nativestudy.domain.auth.dto.SignupRequest
import com.teamnative.nativestudy.domain.auth.dto.SignupResponse
import com.teamnative.nativestudy.domain.auth.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
        LoginResponse(
            accessToken = "replace-with-jwt-token",
            tokenType = "Bearer",
            username = request.username,
        )
}
