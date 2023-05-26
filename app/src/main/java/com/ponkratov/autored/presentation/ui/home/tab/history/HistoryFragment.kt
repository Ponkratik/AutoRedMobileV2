package com.ponkratov.autored.presentation.ui.home.tab.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.ponkratov.autored.databinding.FragmentHistoryBinding
import com.ponkratov.autored.domain.model.Lce
import com.ponkratov.autored.presentation.extensions.addVerticalSpace
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel by viewModel<HistoryViewModel>()

    private val adapter by lazy {
        RideAdapter(
            context = requireContext(),
            onRideClicked = {
                findNavController().navigate(
                    HistoryFragmentDirections.actionHistoryToDetails(Gson().toJson(it))
                )
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentHistoryBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            layoutSwiperefresh.isRefreshing = true

            advertisementRecyclerView.adapter = adapter
            advertisementRecyclerView.addVerticalSpace()

            layoutSwiperefresh.setOnRefreshListener {
                viewModel.onRefreshSwiped()
            }

            viewModel
                .lceFlow
                .onEach {
                    when (it) {
                        is Lce.Loading -> {
                            layoutSwiperefresh.isRefreshing = true
                        }
                        is Lce.Content -> {
                            adapter.submitList(it.data)
                            layoutSwiperefresh.isRefreshing = false
                        }
                        is Lce.Error -> {
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