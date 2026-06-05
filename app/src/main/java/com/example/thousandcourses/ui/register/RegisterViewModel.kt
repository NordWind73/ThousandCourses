package com.example.thousandcourses.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thousandcourses.R
import com.example.domain.model.RegistrationResult
import com.example.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState: StateFlow<RegistrationState> = _registrationState.asStateFlow()

    private val _navigateToLogin = MutableStateFlow(false)
    val navigateToLogin: StateFlow<Boolean> = _navigateToLogin.asStateFlow()

    private val EMAIL_PATTERN = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")

    fun isEmailValid(email: String): Boolean {
        return EMAIL_PATTERN.matches(email) && !email.matches(Regex(".*[а-яА-ЯёЁ].*"))
    }

    fun register(emailInput: String, password: String, confirmPassword: String, codeWord: String) {
        val email = emailInput.lowercase().trim()
        viewModelScope.launch {
            _registrationState.value = RegistrationState.Loading
            _navigateToLogin.value = false

            when {
                email.isBlank() -> {
                    _registrationState.value = RegistrationState.Error(R.string.error_email_empty)
                    return@launch
                }
                password.isBlank() -> {
                    _registrationState.value = RegistrationState.Error(R.string.error_password_empty)
                    return@launch
                }
                !isEmailValid(email) -> {
                    _registrationState.value = RegistrationState.Error(R.string.error_email_invalid)
                    return@launch
                }
                password.length < 6 -> {
                    _registrationState.value = RegistrationState.Error(R.string.error_password_short)
                    return@launch
                }
                password != confirmPassword -> {
                    _registrationState.value = RegistrationState.Error(R.string.error_passwords_not_match)
                    return@launch
                }
                codeWord.isBlank() -> {
                    _registrationState.value = RegistrationState.Error(R.string.error_codeword_empty)
                    return@launch
                }
            }

            val result = registerUseCase(email, password, codeWord)
            _registrationState.value = when (result) {
                is RegistrationResult.Success -> {
                    _navigateToLogin.value = true
                    RegistrationState.Success
                }
                is RegistrationResult.Error -> {
                    RegistrationState.Error(result.message)
                }
            }
        }
    }

    fun resetNavigation() {
        _navigateToLogin.value = false
    }

    sealed class RegistrationState {
        data object Idle : RegistrationState()
        data object Loading : RegistrationState()
        data object Success : RegistrationState()
        data class Error(val message: Int) : RegistrationState()
    }

    fun resetErrorState() {
        _registrationState.value = RegistrationState.Idle
    }
}