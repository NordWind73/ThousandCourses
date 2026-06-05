package com.example.thousandcourses.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thousandcourses.R
import com.example.thousandcourses.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val viewModel: HomeViewModel by viewModels()

    private val courseAdapter by lazy {
     CourseAdapter(
        onFavoriteClick = { courseId ->
            viewModel.toggleFavorite(courseId)
        },
        onDetailsClick = { courseId ->
            navigateToCourseDetails(courseId)
        }
    )}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshCourses()
    }

    private fun setupRecyclerView() {
        binding.rvCourses.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = courseAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (_binding == null) return@collect

                when (state) {
                    HomeUiState.Loading -> {
                        showLoading(true)
                        showError(null)
                        showRecyclerView(false)
                    }

                    is HomeUiState.Success -> {
                        showLoading(false)
                        showError(null)
                        showRecyclerView(true)

                        courseAdapter.submitList(state.courses)
                    }

                    is HomeUiState.Error -> {
                        showLoading(false)
                        showError(state.message)
                        showRecyclerView(false)
                    }
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnRefresh.setOnClickListener {
            viewModel.refreshCourses()
        }

        binding.btnFilter.setOnClickListener {
            viewModel.toggleSort()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filterCourses(newText.orEmpty())
                return true
            }
        })
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showError(message: String?) {
        if (message != null) {
            binding.tvError.text = message
            binding.tvError.visibility = View.VISIBLE
        } else {
            binding.tvError.visibility = View.GONE
        }
    }

    private fun showRecyclerView(show: Boolean) {
        binding.rvCourses.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun navigateToCourseDetails(courseId: String) {
        val bundle = Bundle().apply {
            putString(COURSE_ID, courseId)
        }
        findNavController().navigate(
            R.id.action_homeFragment_to_courseDetailsFragment,
            bundle
        )
    }
    companion object {
        private const val COURSE_ID = "courseId"
    }
}