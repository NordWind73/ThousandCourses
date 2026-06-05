package com.example.domain.usecase

import com.example.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): Boolean {
        return userRepository.login(email, password)
    }
}