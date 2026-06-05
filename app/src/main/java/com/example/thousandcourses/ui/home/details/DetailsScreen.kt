package com.example.thousandcourses.ui.home.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.thousandcourses.R
import com.example.thousandcourses.ui.home.getImageForCourse

@Composable
fun DetailsScreen(
    viewModel: CourseDetailsViewModel,
    courseId: String,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    androidx.compose.runtime.LaunchedEffect(courseId) {
        viewModel.loadCourse(courseId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_dark))
    ) {
        when (val state = uiState) {
            is DetailsUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = colorResource(R.color.accent_green)
                    )
                }
            }

            is DetailsUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.message,
                        color = Color.Red,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onBackClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.accent_green_dark)
                        )
                    ) {
                        Text(stringResource(R.string.back))
                    }
                }
            }

            is DetailsUiState.Success -> {
                DetailsContent(
                    course = state.course,
                    onFavoriteClick = { viewModel.toggleFavorite() },
                    onEnrollClick = {
                        android.widget.Toast.makeText(
                            context,
                            context.getString(R.string.enrollment_message, state.course.title),
                            android.widget.Toast.LENGTH_SHORT,
                        ).show()
                    },
                    onPlatformClick = {
                        android.widget.Toast.makeText(
                            context,
                            context.getString(R.string.go_to_platform),
                            android.widget.Toast.LENGTH_SHORT,
                        ).show()
                    },
                    onBackClick = onBackClick
                )
            }
        }
    }
}

@Composable
fun DetailsContent(
    course: CourseDetailsUi,
    onFavoriteClick: () -> Unit,
    onEnrollClick: () -> Unit,
    onPlatformClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(16.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(50)
                )
                .size(40.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = stringResource(R.string.back),
                tint = Color.White,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(top = 24.dp)
        ) {
            Image(
                painter = painterResource(id = getImageForCourse(course.id)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.card_default))
            )
            Image(
                painter = painterResource(
                    id = if (course.isFavorite) R.drawable.ic_favorite_filled
                    else R.drawable.ic_favorite_border
                ),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colorResource(R.color.accent_green)),
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.TopEnd)
                    .clickable { onFavoriteClick() }
            )
            Text(
                text = stringResource(R.string.rating_with_star, course.rate.toDoubleOrNull() ?: 0.0),
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 8.dp, bottom = 8.dp)
                    .background(
                        color = colorResource(R.color.accent_green),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
            Text(
                text = stringResource(R.string.date_with_calendar, course.startDate),
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 8.dp, bottom = 8.dp)
                    .background(
                        color = colorResource(R.color.blue),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = course.title,
                color = colorResource(R.color.on_surface_white),
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 24.dp),
            )
            Text(
                text = course.price,
                color = colorResource(R.color.accent_green),
                fontSize = 22.sp,
                modifier = Modifier.padding(top = 16.dp),
            )
            Text(
                text = course.fullDescription,
                color = colorResource(R.color.on_surface_grey),
                fontSize = 16.sp,
                lineHeight = 20.sp,
                modifier = Modifier.padding(top = 24.dp),
            )
            Text(
                text = stringResource(R.string.published_date_format, course.publishDate),
                color = colorResource(R.color.on_surface_grey),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 16.dp),
            )
            Button(
                onClick = onEnrollClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.accent_green)
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = stringResource(R.string.enroll_on_course),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
            Button(
                onClick = onPlatformClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(top = 16.dp, bottom = 0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.surface_dark)
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = stringResource(R.string.go_to_platform),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}
