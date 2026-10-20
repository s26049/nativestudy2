package com.teamnative.nativestudy.domain.auth.service

import com.teamnative.nativestudy.domain.auth.dto.SignupRequest
import com.teamnative.nativestudy.domain.auth.dto.SignupResponse
import com.teamnative.nativestudy.domain.auth.dto.LoginRequest
import com.teamnative.nativestudy.domain.auth.dto.LoginResponse
import com.teamnative.nativestudy.domain.auth.entity.User
import com.teamnative.nativestudy.domain.auth.repository.UserRepository
import com.teamnative.nativestudy.global.exception.ConflictException
import com.teamnative.nativestudy.global.exception.InvalidRequestException
import com.teamnative.nativestudy.global.exception.UnauthorizedException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val accessTokenService: AccessTokenService,
) {
    @Transactional(readOnly = true)
    fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByUsername(request.username)
        if (user == null || !passwordEncoder.matches(request.password, user.password)) {
            throw UnauthorizedException("Invalid username or password.")
        }

        return LoginResponse(
            accessToken = accessTokenService.issue(user.username),
            tokenType = "Bearer",
            username = user.username,
        )
    }

    fun logout(token: String) {
        accessTokenService.revoke(token)
    }

    @Transactional
    fun deleteAccount(username: String, token: String?) {
        val user = userRepository.findByUsername(username)
            ?: throw UnauthorizedException("Authenticated account was not found.")
        userRepository.delete(user)
        token?.let(accessTokenService::revoke)
    }

    @Transactional
    fun signup(request: SignupRequest): SignupResponse {
        if (request.password != request.confirmPassword) {
            throw InvalidRequestException("비밀번호와 비밀번호 확인이 일치하지 않습니다.")
        }
        if (userRepository.existsByUsername(request.id)) {
            throw ConflictException("이미 사용 중인 ID입니다.")
        }

        val encodedPassword = requireNotNull(passwordEncoder.encode(request.password))
        try {
            userRepository.saveAndFlush(
                User(
                    username = request.id,
                    password = encodedPassword,
                ),
            )
        } catch (exception: DataIntegrityViolationException) {
            throw ConflictException("이미 사용 중인 ID입니다.")
        }
        return SignupResponse(id = request.id)
    }
}
