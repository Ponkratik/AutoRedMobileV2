package com.ponkratov.autored.presentation.ui.home.tab.account.ridelist

import androidx.recyclerview.widget.RecyclerView
import com.ponkratov.autored.databinding.ItemRideLessorBinding
import com.ponkratov.autored.domain.model.response.RideResponse
import java.text.SimpleDateFormat
import java.util.*

class RideLessorViewHolder(
    private val binding: ItemRideLessorBinding,
    private val onRideItemClicked: (RideResponse) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RideResponse) {
        with(binding) {
            lessorFio.text = item.user.fio

            textDateStart.text =
                SimpleDateFormat("dd.MM.yyyy", Locale.US).format(item.ride.dateStart)

            textDateEnd.text = if (item.ride.dateEnd == Date(0)) {
                "по н.в."
            } else {
                SimpleDateFormat("dd.MM.yyyy", Locale.US).format(item.ride.dateEnd)
            }

            root.setOnClickListener {
                onRideItemClicked(item)
            }
        }
    }
}