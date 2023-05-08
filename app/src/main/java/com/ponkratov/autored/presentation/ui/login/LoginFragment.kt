package com.ponkratov.autored.presentation.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ponkratov.autored.databinding.FragmentLoginBinding
import com.ponkratov.autored.domain.model.Lce
import com.ponkratov.autored.presentation.extensions.hideKeyboard
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel by viewModel<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentLoginBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            progressCircular.isVisible = false
            layoutLogin.isVisible = true

            buttonRegister.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginToRegister())
            }

            buttonLogin.setOnClickListener {
                if (editTextEmail.text.toString().isEmpty()) {
                    containerEmail.error = "Поле пусто"
                    return@setOnClickListener
                }

                if (editTextPassword.text.toString().isEmpty()) {
                    containerPassword.error = "Поле пусто"
                    return@setOnClickListener
                }

                viewModel.onLoginButtonClicked(
                    editTextEmail.text.toString(),
                    editTextPassword.text.toString()
                )
                hideKeyboard()
            }

            viewModel
                .lceFlow
                .onEach {
                    when (it) {
                        is Lce.Loading -> {
                            progressCircular.isVisible = true
                            layoutLogin.isVisible = false
                        }
                        is Lce.Content -> {
                            progressCircular.isVisible = false
                            findNavController().navigate(LoginFragmentDirections.actionLoginToHome())
                        }
                        is Lce.Error -> {
                            progressCircular.isVisible = false
                            layoutLogin.isVisible = true
                            Snackbar.make(
                                requireView(),
                                it.toString(),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}