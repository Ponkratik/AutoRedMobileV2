package com.ponkratov.autored.presentation.ui.home.tab.search.details

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ponkratov.autored.databinding.ItemImageBinding

class ImageViewHolder(
    private val binding: ItemImageBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(filePath: String) {
        with(binding) {
            carPhoto.load(filePath)
        }
    }
}