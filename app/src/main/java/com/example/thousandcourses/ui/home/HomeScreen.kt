package com.example.thousandcourses.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.thousandcourses.R

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onCourseClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.refreshCourses()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.black))
            .padding(dimensionResource(R.dimen.padding_large))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.home_title),
                color = colorResource(R.color.on_surface_white),
                fontSize = dimensionResource(R.dimen.text_size_title).value.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            androidx.compose.material3.Button(
                onClick = { viewModel.refreshCourses() },
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.accent_green_dark)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.refresh),
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
        var searchQuery by remember { mutableStateOf("") }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { query ->
                    searchQuery = query
                    viewModel.filterCourses(query)
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text("", color = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.DarkGray,
                    unfocusedContainerColor = Color.DarkGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )
            IconButton(
                onClick = { viewModel.toggleSort() },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter),
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        when (uiState) {
            is HomeUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = colorResource(R.color.accent_green)
                    )
                }
            }

            is HomeUiState.Error -> {
                val errorState = uiState as HomeUiState.Error
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorState.message,
                        color = Color.Red,
                        fontSize = 16.sp
                    )
                }
            }

            is HomeUiState.Success -> {
                val successState = uiState as HomeUiState.Success

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = successState.courses,
                        key = { course -> course.id }
                    ) { course ->
                        CourseCard(
                            course = course,
                            onFavoriteClick = { viewModel.toggleFavorite(course.id) },
                            onDetailsClick = { onCourseClick(course.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CourseCard(
    course: CourseUi,
    onFavoriteClick: () -> Unit,
    onDetailsClick: () -> Unit
) {
    val cleanPrice = course.price.replace(" ", "").replace(",", ".").toDoubleOrNull() ?: 0.0
    val cleanRate = course.rate.replace(" ", "").replace(",", ".").toDoubleOrNull() ?: 0.0
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.surface_dark)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = getImageForCourse(course.id)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(colorResource(R.color.card_default))
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = course.title,
                    color = colorResource(R.color.on_surface_white),
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )

                Image(
                    painter = painterResource(
                        id = if (course.isFavorite) R.drawable.ic_favorite_filled
                        else R.drawable.ic_favorite_border
                    ),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colorResource(R.color.on_surface_grey)),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onFavoriteClick() }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = course.text,
                color = colorResource(R.color.on_surface_grey),
                fontSize = 14.sp,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.price_with_ruble, cleanPrice),
                    color = colorResource(R.color.accent_green),
                    fontSize = 16.sp
                )
                Text(
                    text = stringResource(R.string.rating_with_star, cleanRate),
                    color = colorResource(R.color.on_surface_grey),
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.start_date, course.startDate),
                    color = colorResource(R.color.on_surface_grey),
                    fontSize = 12.sp
                )
                Text(
                    text = stringResource(R.string.details),
                    color = colorResource(R.color.accent_green),
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onDetailsClick() }
                )
            }
        }
    }
}
fun getImageForCourse(courseId: String): Int {
    val courseImages = mapOf(
        "100" to R.drawable.course_default_1,
        "101" to R.drawable.course_default_2,
        "102" to R.drawable.course_default_3,
        "103" to R.drawable.course_default_4,
        "104" to R.drawable.course_default_5,
    )

    val fallbackImages = listOf(
        R.drawable.course_default_1,
        R.drawable.course_default_2,
        R.drawable.course_default_3,
        R.drawable.course_default_4,
        R.drawable.course_default_5,
        R.drawable.course_default_6,
    )

    return courseImages[courseId] ?: fallbackImages.random()
}
