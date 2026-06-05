package com.example.thousandcourses.ui.login

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.thousandcourses.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.net.toUri
import com.example.data.BuildConfig

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val context = LocalContext.current
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()
    val navigateToHome by viewModel.navigateToHome.collectAsStateWithLifecycle(initialValue = null)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(navigateToHome) {
        if (navigateToHome != null) {
            onNavigateToHome()
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(R.color.background_dark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(dimensionResource(R.dimen.padding_large)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, bottom = dimensionResource(R.dimen.padding_large))
            ) {
                Text(
                    text = stringResource(R.string.title_thousand_courses),
                    color = colorResource(R.color.on_surface_white),
                    fontSize = dimensionResource(R.dimen.text_size_title).value.sp,
                    modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small))
                )
            }

            Text(
                text = stringResource(R.string.email_title),
                color = colorResource(R.color.on_surface_white),
                fontSize = dimensionResource(R.dimen.text_size_button).value.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(R.dimen.margin_vertical))
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(R.dimen.margin_vertical)),
                placeholder = {
                    Text(
                        text = stringResource(R.string.email_hint),
                        color = colorResource(R.color.on_surface_grey)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = colorResource(R.color.on_surface_white),
                    unfocusedTextColor = colorResource(R.color.on_surface_white),
                    focusedBorderColor = colorResource(R.color.on_surface_grey),
                    unfocusedBorderColor = colorResource(R.color.on_surface_grey),
                    focusedPlaceholderColor = colorResource(R.color.on_surface_grey),
                    unfocusedPlaceholderColor = colorResource(R.color.on_surface_grey)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            Text(
                text = stringResource(R.string.password_title),
                color = colorResource(R.color.on_surface_white),
                fontSize = dimensionResource(R.dimen.text_size_button).value.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(R.dimen.margin_vertical))
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(R.dimen.padding_medium)),
                placeholder = {
                    Text(
                        text = stringResource(R.string.password_hint),
                        color = colorResource(R.color.on_surface_grey)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = colorResource(R.color.on_surface_white),
                    unfocusedTextColor = colorResource(R.color.on_surface_white),
                    focusedBorderColor = colorResource(R.color.on_surface_grey),
                    unfocusedBorderColor = colorResource(R.color.on_surface_grey),
                    focusedPlaceholderColor = colorResource(R.color.on_surface_grey),
                    unfocusedPlaceholderColor = colorResource(R.color.on_surface_grey)
                ),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            Button(
                onClick = {
                    viewModel.login(email, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.button_height)),
                enabled = loginState != LoginViewModel.LoginState.Loading,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.accent_green_dark),
                    disabledContainerColor = colorResource(R.color.accent_green_dark).copy(alpha = 0.5f)
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
            ) {
                when (loginState) {
                    LoginViewModel.LoginState.Loading -> {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.height(24.dp)
                        )
                    }

                    LoginViewModel.LoginState.Error -> {
                        Text(
                            text = stringResource(R.string.error),
                            color = Color.White,
                            fontSize = dimensionResource(R.dimen.text_size_button).value.sp
                        )
                    }

                    else -> {
                        Text(
                            text = stringResource(R.string.login),
                            color = Color.White,
                            fontSize = dimensionResource(R.dimen.text_size_button).value.sp
                        )
                    }
                }
            }

            Text(
                text = stringResource(R.string.register),
                color = colorResource(R.color.accent_green),
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = dimensionResource(R.dimen.padding_small))
                    .clickable { onNavigateToRegister() }
            )

            Text(
                text = stringResource(R.string.forgot_password),
                color = colorResource(R.color.accent_green),
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp)
                    .clickable { }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.button_vk),
                    contentDescription = "VK",
                    modifier = Modifier
                        .size(128.dp)
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, BuildConfig.VK_LINK.toUri())
                            context.startActivity(intent)
                        }
                )

                Spacer(modifier = Modifier.width(32.dp))

                Image(
                    painter = painterResource(id = R.drawable.button_ok),
                    contentDescription = "OK",
                    modifier = Modifier
                        .size(128.dp)
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, BuildConfig.OK_LINK.toUri())
                            context.startActivity(intent)
                        }
                )
            }
        }
    }
}