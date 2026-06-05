package com.example.domain.repository

import com.example.domain.model.RegistrationResult

interface UserRepository {
    suspend fun login(email: String, password: String): Boolean
    suspend fun register(email: String, password: String, codeWord: String): RegistrationResult
    suspend fun initializeTestUser()
}