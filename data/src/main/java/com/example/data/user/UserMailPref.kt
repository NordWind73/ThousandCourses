package com.example.data.user

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.domain.repository.UserDataStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserMailPref @Inject constructor(
    context: Context
): UserDataStore {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_EMAIL = "user_email"
        private const val SHARED_PREFS_NAME = "app_prefs"
    }

    override fun saveUserEmail(email: String) {
        prefs.edit { putString(KEY_USER_EMAIL, email) }
    }

    override fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }

    override fun clearUserEmail() {
        prefs.edit { remove(KEY_USER_EMAIL) }
    }
}
