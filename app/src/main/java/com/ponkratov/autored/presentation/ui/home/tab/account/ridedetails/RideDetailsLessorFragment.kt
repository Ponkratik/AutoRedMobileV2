package com.ponkratov.autored.presentation.ui.home.tab.account.ridedetails

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.ponkratov.autored.R
import com.ponkratov.autored.databinding.FragmentRideDetailsLesseeBinding
import com.ponkratov.autored.databinding.FragmentRideDetailsLessorBinding
import com.ponkratov.autored.domain.model.Lce
import com.ponkratov.autored.domain.model.RideStatusEnum
import com.ponkratov.autored.domain.model.response.RideResponse
import com.ponkratov.autored.presentation.extensions.addHorisontalSpace
import com.ponkratov.autored.presentation.ui.home.tab.history.details.CheckupDialogLessee
import com.ponkratov.autored.presentation.ui.home.tab.history.details.RideDetailsLesseeFragmentArgs
import com.ponkratov.autored.presentation.ui.home.tab.history.details.RideDetailsLesseeFragmentDirections
import com.ponkratov.autored.presentation.ui.home.tab.history.details.RideDetailsLesseeViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class RideDetailsLessorFragment : Fragment() {

    private var _binding: FragmentRideDetailsLessorBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel by viewModel<RideDetailsLessorViewModel>()

    private val args by navArgs<RideDetailsLessorFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentRideDetailsLessorBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            progressCircular.isVisible = false
            layoutRide.isVisible = true

            viewModel.updateInfo(args.rideId)

            viewModel
                .lceFlowInfo
                .onEach {
                    when (it) {
                        is Lce.Loading -> {
                            progressCircular.isVisible = true
                            layoutRide.isVisible = false
                        }

                        is Lce.Content -> {
                            progressCircular.isVisible = false
                            layoutRide.isVisible = true
                            dismissChildDialog()
                            initLayout(requireNotNull(it.data))
                        }

                        is Lce.Error -> {
                            progressCircular.isVisible = false
                            layoutRide.isVisible = true
                            dismissChildDialog()
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

    private fun dismissChildDialog() {
        val d = childFragmentManager.findFragmentByTag("ride_chechup_dialog_after")
        if (d != null) {
            (d as DialogFragment).dismiss()
        }
    }

    private fun initLayout(rideResponse: RideResponse) {
        with(binding) {
            initButtons(rideResponse)

            lesseeFio.text = rideResponse.user.fio
            textRate.text = rideResponse.advertisementResponse.avgMark.toString()

            textTotalCost.text =
                getString(R.string.text_price, rideResponse.ride.totalCost)

            textReview.setOnClickListener {
                findNavController().navigate(
                    RideDetailsLessorFragmentDirections.actionRideDetailsLessorToAddReview(
                        rideResponse.user.id
                    )
                )
            }

            textLinkChat.setOnClickListener {
                val browserIntent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(rideResponse.ride.chatLink + rideResponse.user.phone)
                    )
                startActivity(browserIntent)
            }

            buttonBack.setOnClickListener {
                findNavController().navigateUp()
            }

            buttonSignAct.setOnClickListener {
                AlertDialog
                    .Builder(requireContext())
                    .setTitle("Начало поездки")
                    .setMessage("Нажимая на кнопку \"ОК\" вы подтверждаете ознакомление с условиями сервиса")
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        viewModel.signActBefore(rideResponse.ride.id)
                    }
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
            }

            buttonEndRide.setOnClickListener {
                CheckupDialogLessor(rideResponse.ride.id).show(
                    childFragmentManager,
                    "ride_chechup_dialog_after"
                )
            }
        }
    }

    private fun initButtons(rideResponse: RideResponse) {
        with(binding) {
            when (rideResponse.ride.statusId) {
                RideStatusEnum.STATUS_BOOKED.id -> {
                    textStatus.text = RideStatusEnum.STATUS_BOOKED.desc
                    infoTable.isVisible = true
                    buttonSignAct.isVisible = false
                    buttonEndRide.isVisible = false
                    textReview.isVisible = false
                }

                RideStatusEnum.STATUS_SIGNED_BEFORE_LESSEE.id -> {
                    textStatus.text = RideStatusEnum.STATUS_SIGNED_BEFORE_LESSEE.desc
                    infoTable.isVisible = true
                    buttonSignAct.isVisible = false
                    buttonEndRide.isVisible = false
                    textReview.isVisible = false
                }

                RideStatusEnum.STATUS_PAYED.id -> {
                    textStatus.text = RideStatusEnum.STATUS_PAYED.desc
                    infoTable.isVisible = true
                    buttonSignAct.isVisible = true
                    buttonEndRide.isVisible = false
                    textReview.isVisible = false
                }

                RideStatusEnum.STATUS_STARTED.id -> {
                    textStatus.text = RideStatusEnum.STATUS_STARTED.desc
                    infoTable.isVisible = true
                    buttonSignAct.isVisible = false
                    buttonEndRide.isVisible = true
                    textReview.isVisible = false
                }

                RideStatusEnum.STATUS_SIGNED_AFTER_LESSOR.id -> {
                    textStatus.text = RideStatusEnum.STATUS_SIGNED_AFTER_LESSOR.desc
                    infoTable.isVisible = true
                    buttonSignAct.isVisible = false
                    buttonEndRide.isVisible = false
                    textReview.isVisible = false
                }

                RideStatusEnum.STATUS_FINISHED.id -> {
                    textStatus.text = RideStatusEnum.STATUS_FINISHED.desc
                    infoTable.isVisible = false
                    buttonSignAct.isVisible = false
                    buttonEndRide.isVisible = false
                    textReview.isVisible = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}