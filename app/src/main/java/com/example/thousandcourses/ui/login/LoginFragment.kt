package com.example.thousandcourses.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.thousandcourses.R
import com.example.thousandcourses.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import com.example.data.BuildConfig

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                when (state) {
                    LoginViewModel.LoginState.Idle -> {
                        binding.btnLogin.isEnabled = true
                        binding.btnLogin.text = getString(R.string.login)
                    }
                    LoginViewModel.LoginState.Loading -> {
                        binding.btnLogin.isEnabled = false
                        binding.btnLogin.text = getString(R.string.logging_in)
                    }
                    LoginViewModel.LoginState.Error -> {
                        binding.btnLogin.text = getString(R.string.error)
                        lifecycleScope.launch {
                            delay(2000)
                            if (viewModel.loginState.value == LoginViewModel.LoginState.Error) {
                                binding.btnLogin.text = getString(R.string.login)
                                binding.btnLogin.isEnabled = true
                            }
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.navigateToHome.collect { shouldNavigate ->
                viewModel.navigateToHome.collect {
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            viewModel.login(email, password)
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.tvForgotPassword.setOnClickListener {}

        binding.ivVk.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, BuildConfig.VK_LINK.toUri())
            startActivity(intent)
            binding.ivOk.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, BuildConfig.OK_LINK.toUri())
                startActivity(intent)
                startActivity(intent)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}