package com.ponkratov.autored.presentation.ui.home.tab.account.ridelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.ponkratov.autored.databinding.FragmentRideListLessorBinding
import com.ponkratov.autored.domain.model.Lce
import com.ponkratov.autored.domain.model.response.AdvertisementResponse
import com.ponkratov.autored.presentation.extensions.addVerticalSpace
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class RideListLessorFragment : Fragment() {
    private var _binding: FragmentRideListLessorBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel by viewModel<RideListLessorViewModel>()

    private val args by navArgs<RideListLessorFragmentArgs>()

    private val adapter by lazy {
        RideLessorAdapter(
            context = requireContext(),
            onRideClicked = {
                findNavController().navigate(
                    RideListLessorFragmentDirections.actionRideListToRideDetailsLessor(Gson().toJson(it))
                )
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentRideListLessorBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val advertisementResponse = Gson().fromJson(args.advertisementResponse, AdvertisementResponse::class.java)
            layoutSwiperefresh.isRefreshing = true
            viewModel.onRefreshSwiped(advertisementResponse.advertisement.id)

            layoutSwiperefresh.setOnRefreshListener {
                viewModel.onRefreshSwiped(advertisementResponse.advertisement.id)
            }

            buttonBack.setOnClickListener {
                findNavController().navigateUp()
            }

            advertisementRecyclerView.adapter = adapter
            advertisementRecyclerView.addVerticalSpace()

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