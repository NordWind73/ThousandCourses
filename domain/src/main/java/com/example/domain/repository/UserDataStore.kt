package com.example.domain.repository

interface UserDataStore {
    fun saveUserEmail(email: String)
    fun getUserEmail(): String?
    fun clearUserEmail()
}