package com.example.thousandcourses.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import com.example.thousandcourses.R
import com.example.thousandcourses.databinding.FragmentOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val viewModel: OnboardingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        setupObservers()
    }

    private fun setupClickListeners() {
        binding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.action_onboarding_to_login)
        }

        binding.cardAdmin.setOnClickListener { viewModel.toggleCategory(1) }
        binding.cardTraffic.setOnClickListener { viewModel.toggleCategory(2) }
        binding.cardMarketing.setOnClickListener { viewModel.toggleCategory(3) }
        binding.cardB28Marketing.setOnClickListener { viewModel.toggleCategory(4) }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.categories.collect { categories ->
                updateCard(binding.cardAdmin, categories[0])
                updateCard(binding.cardTraffic, categories[1])
                updateCard(binding.cardMarketing, categories[2])
                updateCard(binding.cardB28Marketing, categories[3])
            }
        }
    }

    private fun updateCard(card: MaterialCardView, category: CategoryUi) {
        if (category.isSelected) {
            card.setCardBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.card_selected)
            )
            card.strokeColor = ContextCompat.getColor(requireContext(), R.color.accent_green)
            card.strokeWidth = 2
        } else {
            card.setCardBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.card_default)
            )
            card.strokeColor = android.graphics.Color.TRANSPARENT
            card.strokeWidth = 0
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}