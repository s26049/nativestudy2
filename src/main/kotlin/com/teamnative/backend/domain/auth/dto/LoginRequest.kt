package com.teamnative.backend.domain.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:Schema(description = "아이디", example = "user1")
    @field:NotBlank(message = "Username is required.")
    val username: String,

    @field:Schema(description = "비밀번호", example = "password123")
    @field:NotBlank(message = "Password is required.")
    val password: String,
)
