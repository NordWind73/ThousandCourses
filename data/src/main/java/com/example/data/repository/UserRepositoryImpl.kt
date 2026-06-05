package com.example.data.repository


import com.example.data.R
import com.example.data.user.UserDao
import com.example.data.user.UserEntity
import com.example.data.utils.PasswordHasher
import com.example.domain.model.RegistrationResult
import com.example.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepositoryImpl
@Inject constructor(
    private val userDao: UserDao,
    private val passwordHasher: PasswordHasher,
) : UserRepository {

    override suspend fun login(email: String, password: String): Boolean {
        val credentials = userDao.getUserCredentials(email) ?: return false
        return passwordHasher.verifyPassword(password, credentials.salt, credentials.password)
    }

    override suspend fun register(email: String, password: String, codeWord: String): RegistrationResult {
        return try {
            if (userDao.checkIfUserExists(email) > 0) {
                RegistrationResult.Error(R.string.error_user_exists)
            } else {
                val (hash, salt) = passwordHasher.createHash(password)
                userDao.insertUser(UserEntity(email, hash, salt, codeWord))
                RegistrationResult.Success
            }
        } catch (e: Exception) {
            RegistrationResult.Error(R.string.error_registration_failed)
        }
    }

    override suspend fun initializeTestUser() {
        val exists = userDao.checkIfUserExists("test") > 0
        if (!exists) {
            val (hash, salt) = passwordHasher.createHash("test")
            userDao.insertUser(UserEntity("test", hash, salt, "test"))
        }
    }
}