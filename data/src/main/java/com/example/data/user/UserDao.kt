package com.example.data.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT password, salt FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserCredentials(email: String): UserCredentials?
    @Insert(onConflict = OnConflictStrategy.Companion.ABORT)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    suspend fun checkIfUserExists(email: String): Int
}
