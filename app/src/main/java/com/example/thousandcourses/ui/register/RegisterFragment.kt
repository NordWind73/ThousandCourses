package com.example.thousandcourses.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.thousandcourses.R
import com.example.thousandcourses.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.registrationState.collect { state ->
                if (!isAdded || _binding == null) return@collect
                when (state) {
                    RegisterViewModel.RegistrationState.Idle -> {
                        binding.btnRegister.isEnabled = true
                        binding.btnRegister.text = getString(R.string.register)
                        clearErrors()
                    }
                    RegisterViewModel.RegistrationState.Loading -> {
                        binding.btnRegister.isEnabled = false
                        binding.btnRegister.text = getString(R.string.register_processing)
                        clearErrors()
                    }
                    RegisterViewModel.RegistrationState.Success -> {
                        binding.btnRegister.isEnabled = true
                        binding.btnRegister.text = getString(R.string.register_success)
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.registration_successful),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is RegisterViewModel.RegistrationState.Error -> {
                        binding.btnRegister.text = getString(R.string.error)
                        Toast.makeText(
                            requireContext(),
                            state.message,
                            Toast.LENGTH_LONG
                        ).show()

                        lifecycleScope.launch {
                            delay(2000)
                            if (viewModel.registrationState.value is RegisterViewModel.RegistrationState.Error) {
                                binding.btnRegister.text = getString(R.string.register)
                                binding.btnRegister.isEnabled = true
                            }
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.navigateToLogin.collect { shouldNavigate ->
                if (shouldNavigate) {
                    delay(2000)
                    findNavController().navigateUp()
                    viewModel.resetNavigation()
                }
            }
        }
    }
    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()
            val codeWord = binding.etCodeWord.text.toString().trim()

            viewModel.register(email, password, confirmPassword, codeWord)
        }

        binding.etEmail.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) clearErrors()
        }
        binding.etPassword.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) clearErrors()
        }
        binding.etConfirmPassword.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) clearErrors()
        }
        binding.etCodeWord.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) clearErrors()
        }
    }

    private fun clearErrors() {
        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.tilConfirmPassword.error = null
        binding.tilCodeWord.error = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}