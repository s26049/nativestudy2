package com.teamnative.backend.domain.auth.dto

import io.swagger.v3.oas.annotations.media.Schema

data class LoginResponse(
    @field:Schema(description = "액세스 토큰")
    val accessToken: String,

    @field:Schema(description = "토큰 타입", example = "Bearer")
    val tokenType: String,

    @field:Schema(description = "아이디")
    val username: String,
)
