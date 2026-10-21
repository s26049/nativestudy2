package com.teamnative.nativestudy.domain.sample.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Sample", description = "샘플 API")
@RestController
class SampleController {

    @Operation(summary = "공개 핑", description = "인증 없이 호출 가능한 핑 API입니다.")
    @GetMapping("/api/v1/public/ping")
    fun publicPing(): Map<String, String> = mapOf("message" to "public pong")

    @Operation(summary = "비공개 핑", description = "인증이 필요한 핑 API입니다.")
    @GetMapping("/api/v1/private/ping")
    fun privatePing(): Map<String, String> = mapOf("message" to "private pong")

    @Operation(summary = "Authenticated message", description = "Returns Hello World to authenticated users.")
    @GetMapping("/message")
    fun message(): String = "Hello World"
}
