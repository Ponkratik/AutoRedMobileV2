package com.ponkratov.autored.presentation.ui.home.tab.history

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ponkratov.autored.R
import com.ponkratov.autored.databinding.ItemRideBinding
import com.ponkratov.autored.domain.model.response.RideResponse
import java.text.SimpleDateFormat
import java.util.Locale

class RideViewHolder(
    private val binding: ItemRideBinding,
    private val onRideItemClicked: (RideResponse) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RideResponse) {
        with(binding) {
            carPhoto.load(item.advertisementResponse.photoPaths.first())
            carName.text = binding.root.context.getString(
                R.string.text_car_make_model_year,
                item.advertisementResponse.car.make,
                item.advertisementResponse.car.model,
                SimpleDateFormat(
                    "yyyy",
                    Locale.US
                ).format(item.advertisementResponse.car.manufacturedYear)
            )

            textDateStart.text =
                SimpleDateFormat("dd.MM.yyyy", Locale.US).format(item.ride.dateStart)

            textDateEnd.text = SimpleDateFormat("dd.MM.yyyy", Locale.US).format(item.ride.dateEnd)

            textPricePerDay.text =
                binding.root.context.getString(R.string.text_price, item.ride.totalCost)

            root.setOnClickListener {
                onRideItemClicked(item)
            }
        }
    }
}