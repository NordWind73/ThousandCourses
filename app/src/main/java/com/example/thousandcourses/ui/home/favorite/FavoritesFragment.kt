package com.example.thousandcourses.ui.home.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.thousandcourses.R
import com.example.thousandcourses.databinding.FragmentFavoritesBinding
import com.example.thousandcourses.databinding.ItemCourseBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val viewModel: FavoritesViewModel by viewModels()
    private val favoritesAdapter = FavoritesAdapter(
        onFavoriteClick = { courseId ->
            viewModel.toggleFavorite(courseId)
        },
        onDetailsClick = { courseId ->
            navigateToCourseDetails(courseId)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        viewModel.loadFavorites()
    }

    private fun setupRecyclerView() {
        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoritesAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (_binding == null) return@collect

                when (state) {
                    FavoritesViewModel.FavoritesUiState.Loading -> {
                        showLoading(true)
                        showEmptyState(false)
                        showError(null)
                        showRecyclerView(false)
                    }

                    is FavoritesViewModel.FavoritesUiState.Success -> {
                        showLoading(false)
                        showEmptyState(state.courses.isEmpty())
                        showError(null)
                        showRecyclerView(!state.courses.isEmpty())

                        favoritesAdapter.submitList(state.courses)
                    }

                    is FavoritesViewModel.FavoritesUiState.Error -> {
                        showLoading(false)
                        showEmptyState(false)
                        showError(state.message)
                        showRecyclerView(false)
                    }
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnRefresh.setOnClickListener {
            viewModel.refreshFavorites()
        }

        binding.btnGoToHome.setOnClickListener {
            findNavController().navigate(
                R.id.homeFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.homeFragment, false)
                    .build()
            )
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showEmptyState(show: Boolean) {
        binding.groupEmptyState.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showRecyclerView(show: Boolean) {
        binding.rvFavorites.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showError(message: String?) {
        binding.tvError.apply {
            visibility = if (message != null) View.VISIBLE else View.GONE
            text = message
        }
    }

    private fun navigateToCourseDetails(courseId: String) {
        val action = FavoritesFragmentDirections.actionFavoritesFragmentToCourseDetailsFragment(courseId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class FavoriteDiffCallback : DiffUtil.ItemCallback<FavoritesViewModel.FavoriteCourseUi>() {
        override fun areItemsTheSame(
            oldItem: FavoritesViewModel.FavoriteCourseUi,
            newItem: FavoritesViewModel.FavoriteCourseUi
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: FavoritesViewModel.FavoriteCourseUi,
            newItem: FavoritesViewModel.FavoriteCourseUi
        ): Boolean = oldItem == newItem
    }
    inner class FavoritesAdapter(
        private val onFavoriteClick: (courseId: String) -> Unit,
        private val onDetailsClick: (courseId: String) -> Unit
    ) : ListAdapter<FavoritesViewModel.FavoriteCourseUi, FavoritesAdapter.FavoriteViewHolder>(
        FavoriteDiffCallback()
    ) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
            val binding = ItemCourseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return FavoriteViewHolder(binding)
        }

        override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
            holder.bind(getItem(position))
        }

        inner class FavoriteViewHolder(
            private val binding: ItemCourseBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(course: FavoritesViewModel.FavoriteCourseUi) {
                binding.apply {
                    val imageRes = getImageForCourse(course.id)
                    ivCourseImage.setImageResource(imageRes)

                    tvTitle.text = course.title
                    tvDescription.text = course.text
                    val cleanPrice = course.price.toCleanDouble()
                    val cleanRate = course.rate.toCleanDouble()
                    tvPrice.text = itemView.context.getString(R.string.price_with_ruble, cleanPrice)
                    tvRating.text = itemView.context.getString(R.string.rating_with_star, cleanRate)
                    tvStartDate.text = requireContext().getString(R.string.start_date, course.startDate)

                    ivFavorite.setImageResource(R.drawable.ic_favorite_filled)

                    ivFavorite.setOnClickListener {
                        onFavoriteClick(course.id)
                    }

                    btnDetails.setOnClickListener {
                        onDetailsClick(course.id)
                    }
                }
            }
            fun String.toCleanDouble(): Double {
                return this.replace(" ", "").replace(",", ".").toDoubleOrNull() ?: 0.0
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
        }
    }
}