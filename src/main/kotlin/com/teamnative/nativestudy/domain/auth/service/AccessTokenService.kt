package com.teamnative.nativestudy.domain.auth.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Base64
import java.util.concurrent.ConcurrentHashMap
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Service
class AccessTokenService(
    @Value("\${JWT_SECRET:local-development-secret-change-before-deploy-2026}") private val secret: String,
) {
    private val objectMapper = ObjectMapper()
    private val revokedTokens = ConcurrentHashMap<String, Long>()

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

    fun revoke(token: String) {
        val expiresAt = tokenExpiration(token) ?: return
        revokedTokens[token] = expiresAt
    }

    fun username(token: String): String? {
        val parts = token.split('.')
        if (parts.size != 3 || revokedTokens.containsKey(token)) return null
        val content = "${parts[0]}.${parts[1]}"
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(secret.toByteArray(Charsets.UTF_8), "HmacSHA256"))
        val expected = mac.doFinal(content.toByteArray(Charsets.US_ASCII))
        val actual = runCatching { Base64.getUrlDecoder().decode(parts[2]) }.getOrNull() ?: return null
        if (!java.security.MessageDigest.isEqual(expected, actual)) return null
        val expiresAt = tokenExpiration(token) ?: return null
        if (expiresAt <= System.currentTimeMillis() / 1000) return null
        return runCatching {
            objectMapper.readTree(Base64.getUrlDecoder().decode(parts[1])).path("sub").asText()
        }.getOrNull()?.takeIf { it.isNotBlank() }
    }

    private fun tokenExpiration(token: String): Long? = runCatching {
        val payload = token.split('.')[1]
        objectMapper.readTree(Base64.getUrlDecoder().decode(payload)).path("exp").asLong()
            .takeIf { it > 0 }
    }.getOrNull()
}
