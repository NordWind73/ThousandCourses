package com.example.data.utils

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordHasher @Inject constructor() {

    companion object {
        private const val SALT_LENGTH = 32
        private const val HASH_ALGORITHM = "SHA-256"
        private const val ITERATIONS = 10000
    }

    fun generateSalt(): String {
        val salt = ByteArray(SALT_LENGTH)
        SecureRandom().nextBytes(salt)
        return Base64.encodeToString(salt, Base64.NO_WRAP)
    }

    fun hashPassword(password: String, salt: String): String {
        var hash = "$password$salt".toByteArray()

        repeat(ITERATIONS) {
            val digest = MessageDigest.getInstance(HASH_ALGORITHM)
            hash = digest.digest(hash)
        }

        return Base64.encodeToString(hash, Base64.NO_WRAP)
    }

    fun verifyPassword(password: String, salt: String, hashedPassword: String): Boolean {
        val hashToVerify = hashPassword(password, salt)
        return hashToVerify == hashedPassword
    }

    fun createHash(password: String): Pair<String, String> {
        val salt = generateSalt()
        val hash = hashPassword(password, salt)
        return Pair(hash, salt)
    }
}