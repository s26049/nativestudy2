package com.teamnative.nativestudy.domain.auth.repository

import com.teamnative.nativestudy.domain.auth.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun existsByUsername(username: String): Boolean
    fun findByUsername(username: String): User?
}
