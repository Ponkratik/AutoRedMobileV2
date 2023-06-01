package com.ponkratov.autored.presentation.ui.home.tab.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.ponkratov.autored.databinding.FragmentAccountBinding
import com.ponkratov.autored.domain.model.Lce
import com.ponkratov.autored.presentation.extensions.addVerticalSpace
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel by viewModel<AccountViewModel>()

    private val adapter by lazy {
        AdvertisementLessorAdapter(
            context = requireContext(),
            onAdvertisementClicked = {
                findNavController().navigate(
                    AccountFragmentDirections.actionAccountToRideListLessor(Gson().toJson(it))
                )
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAccountBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            progressCircular.isVisible = false

            buttonSettings.setOnClickListener {
                findNavController().navigate(AccountFragmentDirections.actionAccountToSettings())
            }

            buttonAddAdvertisement.setOnClickListener {
                findNavController().navigate(AccountFragmentDirections.actionAccountToAdvertisementAdd())
            }

            advertisementRecyclerView.adapter = adapter
            advertisementRecyclerView.addVerticalSpace()

            viewModel
                .lceFlow
                .onEach {
                    when (it) {
                        is Lce.Loading -> {
                            progressCircular.isVisible = true
                        }

                        is Lce.Content -> {
                            adapter.submitList(it.data)
                            progressCircular.isVisible = false
                        }

                        is Lce.Error -> {
                            progressCircular.isVisible = false
                            Snackbar.make(
                                requireView(),
                                it.message.toString(),
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