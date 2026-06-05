package com.example.thousandcourses.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.thousandcourses.R

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    onContinueClick: () -> Unit
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val selectedCount by viewModel.selectedCount.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(R.color.background_dark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    dimensionResource(R.dimen.padding_large)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(R.dimen.padding_large))
            ) {
                Text(
                    text = stringResource(R.string.title_thousand_courses),
                    color = colorResource(R.color.on_surface_white),
                    fontSize = dimensionResource(R.dimen.text_size_title).value.sp,
                    modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small))
                )

                Text(
                    text = stringResource(R.string.subtitle_in_one_place),
                    color = colorResource(R.color.on_surface_grey),
                    fontSize = dimensionResource(R.dimen.text_size_subtitle).value.sp
                )
            }

            Text(
                text = stringResource(R.string.categories_title),
                color = colorResource(R.color.on_surface_white),
                fontSize = dimensionResource(R.dimen.text_size_button).value.sp,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.margin_vertical))
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.category_margin)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.category_margin)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.category_height) * 2 + dimensionResource(R.dimen.category_margin) * 2)
            ) {
                items(categories) { category ->
                    CategoryCard(
                        category = category,
                        onClick = { viewModel.toggleCategory(category.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

            Button(
                onClick = onContinueClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.button_height)),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.accent_green_dark),
                    disabledContainerColor = colorResource(R.color.accent_green_dark).copy(alpha = 0.5f)
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = stringResource(R.string.button_continue),
                    color = Color.White,
                    fontSize = dimensionResource(R.dimen.text_size_button).value.sp
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
        }
    }
}

@Composable
fun CategoryCard(
    category: CategoryUi,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.category_height)),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(
            dimensionResource(R.dimen.category_corner_radius)
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (category.isSelected) {
                colorResource(R.color.card_selected)
            } else {
                colorResource(R.color.card_default)
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = stringResource(category.nameRes),
                color = colorResource(R.color.on_surface_white),
                fontSize = dimensionResource(R.dimen.text_size_category).value.sp,
                style = if (category.isSelected)
                    MaterialTheme.typography.bodyLarge.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                else
                    MaterialTheme.typography.bodyLarge
            )
        }
    }
}