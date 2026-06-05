package com.example.thousandcourses.ui.home.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.thousandcourses.R
import com.example.thousandcourses.databinding.FragmentCourseDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class CourseDetailsFragment : Fragment() {

    private var _binding: FragmentCourseDetailsBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val args: CourseDetailsFragmentArgs by navArgs()

    private val viewModel: CourseDetailsViewModel by viewModels()

    private var timer = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCourseDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        setupObservers()
        viewModel.loadCourse(args.courseId)
    }
    private fun setupClickListeners() {
        binding.ivFavorite.setOnClickListener {
            viewModel.toggleFavorite()
        }

        binding.btnEnroll.setOnClickListener {
            val message = getString(
                R.string.enrollment_message,
                viewModel.currentCourse?.title ?: ""
            )
            showMessage(message)
        }

        binding.btnGoToPlatform.setOnClickListener {
            showMessage(getString(R.string.go_to_platform))
        }
    }

    private fun showMessage (message: String){
        val nowTime = System.currentTimeMillis()
        if (nowTime - timer > 1000){
        timer = nowTime
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    DetailsUiState.Loading -> {}

                    is DetailsUiState.Success -> {
                        bindCourse(state.course)
                    }

                    is DetailsUiState.Error -> {
                        showError(state.message)
                    }
                }
            }
        }
    }
    private fun showError(message: String) {
        Toast.makeText(
            requireContext(),
            getString(R.string.error_prefix, message),
            Toast.LENGTH_LONG
        ).show()
        findNavController().navigateUp()
    }
    private fun bindCourse(course: CourseDetailsUi) {
        binding.apply {
            val imageRes = getImageForCourse(course.id)
            ivCourseImage.setImageResource(imageRes)

            tvCourseTitle.text = course.title
            tvPrice.text = getString(R.string.price_with_ruble, course.price.toDoubleOrNull() ?: 0.0)
            tvRating.text = getString(R.string.rating_with_star, course.rate.toDoubleOrNull() ?: 0.0)
            tvStartDate.text = getString(R.string.date_with_calendar, course.startDate)
            tvPublishDate.text = getString(R.string.published_date_format, course.publishDate)
            tvFullDescription.text = course.fullDescription

            ivFavorite.setImageResource(
                if (course.isFavorite) R.drawable.ic_favorite_filled
                else R.drawable.ic_favorite_border
            )
        }
    }

    private fun getImageForCourse(courseId: String): Int {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}