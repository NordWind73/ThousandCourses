package com.example.thousandcourses.ui.home.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val userDataStore: UserDataStore

) : ViewModel() {

    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail: StateFlow<String?> = _userEmail.asStateFlow()
    private val _shouldLogout = MutableStateFlow(false)
    val shouldLogout: StateFlow<Boolean> = _shouldLogout.asStateFlow()

    init {
        loadUserEmail()
    }
    fun loadUserEmail() {
        _userEmail.value = userDataStore.getUserEmail()
    }
    fun getUserEmail(): String? {
        return userDataStore.getUserEmail()
    }
    fun clearUserData() {
        userDataStore.clearUserEmail()
        _userEmail.value = null
    }
    fun logout() {
        viewModelScope.launch {
            userDataStore.clearUserEmail()
            _shouldLogout.value = true
        }
    }
    fun resetLogoutState() {
        _shouldLogout.value = false
    }
}
