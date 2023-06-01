package com.ponkratov.autored.presentation.ui.home.tab.account

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ponkratov.autored.R
import com.ponkratov.autored.databinding.ItemAdvertisementLessorBinding
import com.ponkratov.autored.domain.model.AdvertisementStatusEnum
import com.ponkratov.autored.domain.model.response.AdvertisementResponse
import java.text.SimpleDateFormat
import java.util.Locale

class AdvertisementLessorViewHolder(
    private val binding: ItemAdvertisementLessorBinding,
    private val onAdvertisementItemClicked: (AdvertisementResponse) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AdvertisementResponse) {
        with(binding) {
            carPhoto.load(item.photoPaths.first())
            carName.text = binding.root.context.getString(
                R.string.text_car_make_model_year,
                item.car.make,
                item.car.model,
                SimpleDateFormat("yyyy", Locale.US).format(item.car.manufacturedYear)
            )
            textRate.text = item.avgMark.toString()
            textPricePerDay.text =
                binding.root.context.getString(R.string.text_price, item.advertisement.pricePerDay)
            textRidesQty.text = binding.root.context.getString(R.string.rides_qty, item.rides)
            textStatus.text = AdvertisementStatusEnum.getDescriptionById(item.advertisement.statusId)

            root.setOnClickListener {
                onAdvertisementItemClicked(item)
            }
        }
    }
}