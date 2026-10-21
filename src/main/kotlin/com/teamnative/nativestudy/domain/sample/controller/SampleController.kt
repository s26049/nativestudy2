package com.teamnative.nativestudy.domain.sample.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Sample", description = "샘플 API")
@RestController
class SampleController {

    @Operation(summary = "Authenticated message", description = "/message path 에 접근했을 때 로그인이 되어 있다면 Hello World 출력, 이외엔 401 status code 반환")
    @GetMapping("/message")
    fun message(): String = "Hello World"
}
