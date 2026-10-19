package com.teamnative.nativestudy.domain.auth.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Service
class AccessTokenService(
    @Value("\${JWT_SECRET:local-development-secret-change-before-deploy-2026}") private val secret: String,
) {
    private val objectMapper = ObjectMapper()
    fun issue(username: String): String {
        val encoder = Base64.getUrlEncoder().withoutPadding()
        val header = encoder.encodeToString(objectMapper.writeValueAsBytes(mapOf("alg" to "HS256", "typ" to "JWT")))
        val payload = encoder.encodeToString(
            objectMapper.writeValueAsBytes(
                mapOf("sub" to username, "iat" to System.currentTimeMillis() / 1000, "exp" to System.currentTimeMillis() / 1000 + 3600),
            ),
        )
        val content = "$header.$payload"
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(secret.toByteArray(Charsets.UTF_8), "HmacSHA256"))
        return "$content.${encoder.encodeToString(mac.doFinal(content.toByteArray(Charsets.US_ASCII)))}"
    }
}
