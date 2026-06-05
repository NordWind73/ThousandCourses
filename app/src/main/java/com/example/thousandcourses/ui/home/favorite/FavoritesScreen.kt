package com.example.thousandcourses.ui.home.favorite

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.thousandcourses.R
import com.example.thousandcourses.ui.home.getImageForCourse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onCourseClick: (String) -> Unit,
    onNavigateToHome: () -> Unit
) {
    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.refreshFavorites()
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.favorites_title),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        containerColor = Color.Black
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
        ) {
            when (uiState) {
                is FavoritesViewModel.FavoritesUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = colorResource(R.color.accent_green)
                        )
                    }
                }

                is FavoritesViewModel.FavoritesUiState.Error -> {
                    val errorState = uiState as FavoritesViewModel.FavoritesUiState.Error
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = errorState.message,
                            color = Color.Red,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.refreshFavorites() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.accent_green_dark)
                            )
                        ) {
                            Text(stringResource(R.string.refresh))
                        }
                    }
                }

                is FavoritesViewModel.FavoritesUiState.Success -> {
                    val successState = uiState as FavoritesViewModel.FavoritesUiState.Success

                    if (successState.courses.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_favorite_border),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(120.dp)
                                    .padding(16.dp),
                                colorFilter = ColorFilter.tint(colorResource(R.color.on_surface_grey))
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = stringResource(R.string.no_favorites),
                                color = colorResource(R.color.on_surface_white),
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = onNavigateToHome,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(R.color.accent_green_dark)
                                ),
                                shape = RoundedCornerShape(28.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.find_courses),
                                    color = Color.White
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = successState.courses,
                                key = { course -> course.id }
                            ) { course ->
                                FavoriteCourseCard(
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
    }
}

@Composable
fun FavoriteCourseCard(
    course: FavoritesViewModel.FavoriteCourseUi,
    onFavoriteClick: () -> Unit,
    onDetailsClick: () -> Unit
) {
    val cleanPrice = course.price.replace(" ", "").replace(",", ".").toDoubleOrNull() ?: 0.0
    val cleanRate = course.rate.replace(" ", "").replace(",", ".").toDoubleOrNull() ?: 0.0
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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
                    painter = painterResource(id = R.drawable.ic_favorite_filled),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colorResource(R.color.accent_green)),
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