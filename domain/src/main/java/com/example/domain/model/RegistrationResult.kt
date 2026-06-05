package com.example.domain.model

sealed class RegistrationResult {
    data object Success : RegistrationResult()
    data class Error(val message: Int) : RegistrationResult()
}