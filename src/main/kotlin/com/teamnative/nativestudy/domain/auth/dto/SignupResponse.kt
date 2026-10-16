package com.teamnative.nativestudy.domain.auth.dto

data class SignupResponse(
    val id: String,
    val message: String = "회원가입이 완료되었습니다.",
)
