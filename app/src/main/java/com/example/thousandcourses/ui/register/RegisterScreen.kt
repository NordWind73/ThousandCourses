package com.example.thousandcourses.ui.register

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.thousandcourses.R
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onNavigateToLogin: () -> Unit
) {
    val registrationState by viewModel.registrationState.collectAsStateWithLifecycle()
    val navigateToLogin by viewModel.navigateToLogin.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var email by rememberSaveable  { mutableStateOf("") }
    var password by rememberSaveable  { mutableStateOf("") }
    var confirmPassword by rememberSaveable  { mutableStateOf("") }
    var codeWord by rememberSaveable  { mutableStateOf("") }

    val errorEmailEmpty = stringResource(R.string.error_email_empty)
    val errorPasswordEmpty = stringResource(R.string.error_password_empty)
    val errorPasswordShort = stringResource(R.string.error_password_short)
    val errorPasswordsNotMatch = stringResource(R.string.error_passwords_not_match)
    val errorCodewordEmpty = stringResource(R.string.error_codeword_empty)
    val errorEmailInvalid = stringResource(R.string.error_email_invalid)

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var codeWordError by remember { mutableStateOf<String?>(null) }

    if (navigateToLogin) {
        onNavigateToLogin()
        viewModel.resetNavigation()
    }

    when (val state = registrationState) {
        is RegisterViewModel.RegistrationState.Error -> {
            val errorMessage = stringResource(state.message)
            androidx.compose.runtime.LaunchedEffect(state) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }

            LaunchedEffect(state) {
                delay(2000)
                viewModel.resetErrorState()
            }
        }
        RegisterViewModel.RegistrationState.Success -> {
            androidx.compose.runtime.LaunchedEffect(Unit) {
                Toast.makeText(
                    context,
                    context.getString(R.string.registration_successful),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        else -> {}
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(dimensionResource(R.dimen.padding_large)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.register),
            color = colorResource(R.color.on_surface_white),
            fontSize = dimensionResource(R.dimen.text_size_title).value.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, bottom = dimensionResource(R.dimen.padding_large))
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.margin_vertical)),
            placeholder = {
                Text(
                    text = stringResource(R.string.email_title),
                    color = colorResource(R.color.on_surface_grey)
                )
            },
            isError = emailError != null,
            supportingText = emailError?.let { { Text(it) } },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = colorResource(R.color.on_surface_white),
                unfocusedTextColor = colorResource(R.color.on_surface_white),
                focusedBorderColor = colorResource(R.color.on_surface_grey),
                unfocusedBorderColor = colorResource(R.color.on_surface_grey),
                focusedPlaceholderColor = colorResource(R.color.on_surface_grey),
                unfocusedPlaceholderColor = colorResource(R.color.on_surface_grey),
                errorBorderColor = Color.Red,
                errorTextColor = Color.Red,
                errorSupportingTextColor = Color.Red
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            enabled = registrationState != RegisterViewModel.RegistrationState.Loading
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.margin_vertical)),
            placeholder = {
                Text(
                    text = stringResource(R.string.password_title),
                    color = colorResource(R.color.on_surface_grey)
                )
            },
            isError = passwordError != null,
            supportingText = passwordError?.let { { Text(it) } },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = colorResource(R.color.on_surface_white),
                unfocusedTextColor = colorResource(R.color.on_surface_white),
                focusedBorderColor = colorResource(R.color.on_surface_grey),
                unfocusedBorderColor = colorResource(R.color.on_surface_grey),
                focusedPlaceholderColor = colorResource(R.color.on_surface_grey),
                unfocusedPlaceholderColor = colorResource(R.color.on_surface_grey),
                errorBorderColor = Color.Red,
                errorTextColor = Color.Red,
                errorSupportingTextColor = Color.Red
            ),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            enabled = registrationState != RegisterViewModel.RegistrationState.Loading
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                confirmPasswordError = null
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.margin_vertical)),
            placeholder = {
                Text(
                    text = stringResource(R.string.confirm_password_hint),
                    color = colorResource(R.color.on_surface_grey)
                )
            },
            isError = confirmPasswordError != null,
            supportingText = confirmPasswordError?.let { { Text(it) } },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = colorResource(R.color.on_surface_white),
                unfocusedTextColor = colorResource(R.color.on_surface_white),
                focusedBorderColor = colorResource(R.color.on_surface_grey),
                unfocusedBorderColor = colorResource(R.color.on_surface_grey),
                focusedPlaceholderColor = colorResource(R.color.on_surface_grey),
                unfocusedPlaceholderColor = colorResource(R.color.on_surface_grey),
                errorBorderColor = Color.Red,
                errorTextColor = Color.Red,
                errorSupportingTextColor = Color.Red
            ),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            enabled = registrationState != RegisterViewModel.RegistrationState.Loading
        )

        OutlinedTextField(
            value = codeWord,
            onValueChange = {
                codeWord = it
                codeWordError = null
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.padding_medium)),
            placeholder = {
                Text(
                    text = stringResource(R.string.code_word_hint),
                    color = colorResource(R.color.on_surface_grey)
                )
            },
            isError = codeWordError != null,
            supportingText = codeWordError?.let { { Text(it) } },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = colorResource(R.color.on_surface_white),
                unfocusedTextColor = colorResource(R.color.on_surface_white),
                focusedBorderColor = colorResource(R.color.on_surface_grey),
                unfocusedBorderColor = colorResource(R.color.on_surface_grey),
                focusedPlaceholderColor = colorResource(R.color.on_surface_grey),
                unfocusedPlaceholderColor = colorResource(R.color.on_surface_grey),
                errorBorderColor = Color.Red,
                errorTextColor = Color.Red,
                errorSupportingTextColor = Color.Red
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            enabled = registrationState != RegisterViewModel.RegistrationState.Loading
        )

        Button(
            onClick = {
                var hasError = false

                if (email.isBlank()) {
                    emailError = errorEmailEmpty
                    hasError = true
                } else if (!viewModel.isEmailValid(email)) {
                    emailError = errorEmailInvalid
                    hasError = true
                }
                if (password.isBlank()) {
                    passwordError = errorPasswordEmpty
                    hasError = true
                } else if (password.length < 6) {
                    passwordError = errorPasswordShort
                    hasError = true
                }
                if (password != confirmPassword) {
                    confirmPasswordError = errorPasswordsNotMatch
                    hasError = true
                }
                if (codeWord.isBlank()) {
                    codeWordError = errorCodewordEmpty
                    hasError = true
                }
                if (!hasError) {
                    viewModel.register(email, password, confirmPassword, codeWord)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.button_height)),
            enabled = registrationState != RegisterViewModel.RegistrationState.Loading,
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.accent_green_dark),
                disabledContainerColor = colorResource(R.color.accent_green_dark).copy(alpha = 0.5f)
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
        ) {
            when (registrationState) {
                RegisterViewModel.RegistrationState.Loading -> {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.height(24.dp)
                    )
                }
                RegisterViewModel.RegistrationState.Success -> {
                    Text(
                        text = stringResource(R.string.register_success),
                        color = Color.White,
                        fontSize = dimensionResource(R.dimen.text_size_button).value.sp
                    )
                }
                else -> {
                    Text(
                        text = stringResource(R.string.register_try),
                        color = Color.White,
                        fontSize = dimensionResource(R.dimen.text_size_button).value.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
    }
}