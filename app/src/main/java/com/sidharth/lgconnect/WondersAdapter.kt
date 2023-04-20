package com.sidharth.lgconnect

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.sidharth.lgconnect.databinding.ItemCardWonderBinding

class WondersAdapter(
    private val context: Context,
    private val wonders: List<Wonder>,
    private val resourceProvider: ResourceProvider
) : Adapter<WondersAdapter.WonderHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WonderHolder {
        val binding = ItemCardWonderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return WonderHolder(binding)
    }

    override fun getItemCount(): Int {
        return wonders.size
    }

    override fun onBindViewHolder(holder: WonderHolder, position: Int) {
        holder.bind(wonders[position], resourceProvider)
    }

    class WonderHolder(private val itemBinding: ItemCardWonderBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(wonder: Wonder, resourceProvider: ResourceProvider) {
            itemBinding.ivWonderCover.setImageDrawable(
                resourceProvider.getDrawable(wonder.cover)
            )

            itemBinding.mcvWonderCard.setOnClickListener {

            }
        }
    }
}