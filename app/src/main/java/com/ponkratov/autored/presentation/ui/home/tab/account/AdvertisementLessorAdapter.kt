package com.ponkratov.autored.presentation.ui.home.tab.account

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ponkratov.autored.databinding.ItemAdvertisementLessorBinding
import com.ponkratov.autored.domain.model.response.AdvertisementResponse

class AdvertisementLessorAdapter(
    context: Context,
    private val onAdvertisementClicked: (AdvertisementResponse) -> Unit
) : ListAdapter<AdvertisementResponse, AdvertisementLessorViewHolder>(DIFF_UTIL) {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdvertisementLessorViewHolder {
        return AdvertisementLessorViewHolder(
            binding = ItemAdvertisementLessorBinding.inflate(layoutInflater, parent, false),
            onAdvertisementItemClicked = onAdvertisementClicked
        )
    }

    override fun onBindViewHolder(holder: AdvertisementLessorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<AdvertisementResponse>() {
            override fun areItemsTheSame(
                oldItem: AdvertisementResponse,
                newItem: AdvertisementResponse
            ): Boolean {
                return oldItem.advertisement.id == newItem.advertisement.id
            }

            override fun areContentsTheSame(
                oldItem: AdvertisementResponse,
                newItem: AdvertisementResponse
            ): Boolean {
                return oldItem.advertisement.id == newItem.advertisement.id
            }

        }
    }
}