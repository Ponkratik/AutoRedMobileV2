package com.ponkratov.autored.presentation.ui.home.tab.history.details

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.ponkratov.autored.R
import com.ponkratov.autored.databinding.FragmentRideDetailsLesseeBinding
import com.ponkratov.autored.domain.model.RideStatusEnum
import com.ponkratov.autored.domain.model.response.RideResponse
import com.ponkratov.autored.presentation.extensions.addHorisontalSpace
import com.ponkratov.autored.presentation.ui.home.tab.search.details.ImageAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class RideDetailsLesseeFragment : Fragment() {

    private var _binding: FragmentRideDetailsLesseeBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel by viewModel<RideDetailsLesseeViewModel>()

    private val args by navArgs<RideDetailsLesseeFragmentArgs>()

    private val imageAdapter by lazy {
        ImageAdapter(
            context = requireContext()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentRideDetailsLesseeBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            progressCircular.isVisible = false
            layoutRide.isVisible = true

            photoRecyclerView.adapter = imageAdapter
            photoRecyclerView.addHorisontalSpace()

            val rideResponse =
                Gson().fromJson(args.rideResponse, RideResponse::class.java)

            initButtons(rideResponse)

            imageAdapter.submitList(rideResponse.advertisementResponse.photoPaths.sortedDescending())

            carName.text = getString(
                R.string.text_car_make_model_year,
                rideResponse.advertisementResponse.car.make,
                rideResponse.advertisementResponse.car.model,
                SimpleDateFormat(
                    "yyyy",
                    Locale.US
                ).format(rideResponse.advertisementResponse.car.manufacturedYear)
            )

            textRate.text = rideResponse.advertisementResponse.avgMark.toString()

            textRidesQty.text = getString(R.string.rides_qty, rideResponse.advertisementResponse.rides)

            textPricePerDay.text =
                getString(
                    R.string.text_price,
                    rideResponse.advertisementResponse.advertisement.pricePerDay
                )
            textPricePerWeek.text =
                getString(
                    R.string.text_price,
                    rideResponse.advertisementResponse.advertisement.pricePerWeek
                )
            textPricePerMonth.text =
                getString(
                    R.string.text_price,
                    rideResponse.advertisementResponse.advertisement.pricePerMonth
                )

            textLinkChat.text = rideResponse.user.email
            textLinkPayment.text = "Перейти"

            textReview.setOnClickListener {
                findNavController().navigate(
                    RideDetailsLesseeFragmentDirections.actionDetailsToReview(
                        rideResponse.advertisementResponse.advertisement.userId,
                        rideResponse.advertisementResponse.car.id
                    )
                )
            }

            textLinkPayment.setOnClickListener {
                findNavController().navigate(RideDetailsLesseeFragmentDirections.actionDetailsToPayment())
            }

            buttonBack.setOnClickListener {
                findNavController().navigateUp()
            }

            buttonSignAct.setOnClickListener {
                AlertDialog
                    .Builder(requireContext())
                    .setTitle("Подписание акта")
                    .setMessage("Нажимая на кнопку \"ОК\" вы подтверждаете ознакомление с условиями сервиса")
                    .setPositiveButton(android.R.string.ok, null)
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
            }

            buttonEndRide.setOnClickListener {
                AlertDialog
                    .Builder(requireContext())
                    .setTitle("Завершение поездки")
                    .setMessage("Нажимая на кнопку \"ОК\" вы подтверждаете ознакомление с условиями сервиса")
                    .setPositiveButton(android.R.string.ok, null)
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
            }
        }

    }

    private fun initButtons(rideResponse: RideResponse) {
        with(binding) {
            when(rideResponse.ride.statusId.toInt()) {
                RideStatusEnum.STATUS_BOOKED.ordinal + 1 -> {
                    textStatus.text = RideStatusEnum.STATUS_BOOKED.desc
                    descriptionTable.isVisible = true
                    buttonSignAct.isVisible = true
                    buttonEndRide.isVisible = false
                    textReview.isVisible = false
                }
                RideStatusEnum.STATUS_SIGNED_BEFORE_LESSEE.ordinal + 1 -> {
                    textStatus.text = RideStatusEnum.STATUS_SIGNED_BEFORE_LESSEE.desc
                    descriptionTable.isVisible = true
                    buttonSignAct.isVisible = false
                    buttonEndRide.isVisible = false
                    textReview.isVisible = false
                }
                RideStatusEnum.STATUS_STARTED.ordinal + 1 -> {
                    textStatus.text = RideStatusEnum.STATUS_STARTED.desc
                    descriptionTable.isVisible = true
                    buttonSignAct.isVisible = false
                    buttonEndRide.isVisible = false
                    textReview.isVisible = false
                }
                RideStatusEnum.STATUS_SIGNED_AFTER_LESSOR.ordinal + 1 -> {
                    textStatus.text = RideStatusEnum.STATUS_SIGNED_AFTER_LESSOR.desc
                    descriptionTable.isVisible = true
                    buttonSignAct.isVisible = false
                    buttonEndRide.isVisible = true
                    textReview.isVisible = false
                }
                RideStatusEnum.STATUS_FINISHED.ordinal + 1 -> {
                    textStatus.text = RideStatusEnum.STATUS_FINISHED.desc
                    descriptionTable.isVisible = false
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