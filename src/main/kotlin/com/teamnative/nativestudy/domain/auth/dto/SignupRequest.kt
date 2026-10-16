package com.teamnative.nativestudy.domain.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class SignupRequest(
    @field:Schema(description = "영문자와 숫자로 구성된 3~8자 ID", example = "user123")
    @field:NotBlank(message = "ID를 입력해 주세요.")
    @field:Size(min = 3, max = 8, message = "ID는 3~8자여야 합니다.")
    @field:Pattern(regexp = "^[A-Za-z0-9]+$", message = "ID는 영문자와 숫자만 사용할 수 있습니다.")
    val id: String,

    @field:Schema(description = "영문자, 숫자, !@#$%^&*로 구성된 5~15자 비밀번호", example = "pass!1")
    @field:NotBlank(message = "비밀번호를 입력해 주세요.")
    @field:Size(min = 5, max = 15, message = "비밀번호는 5~15자여야 합니다.")
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]+$",
        message = "비밀번호는 영문자 또는 숫자를 포함하고, !@#$%^&* 중 특수문자를 하나 이상 포함해야 합니다.",
    )
    val password: String,

    @field:Schema(description = "비밀번호 확인", example = "pass!1")
    @field:NotBlank(message = "비밀번호 확인을 입력해 주세요.")
    val confirmPassword: String,
)
