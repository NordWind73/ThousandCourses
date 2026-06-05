package com.example.domain.usecase

import com.example.domain.model.RegistrationResult
import com.example.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        codeWord: String
    ): RegistrationResult {
        return userRepository.register(email, password, codeWord)
    }
}