package com.sidharth.lgconnect.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.sidharth.lgconnect.databinding.ItemCardWonderBinding
import com.sidharth.lgconnect.domain.model.Wonder
import com.sidharth.lgconnect.ui.home.callback.OnWonderClickCallback
import com.sidharth.lgconnect.util.ResourceProvider
import com.sidharth.lgconnect.util.ToastUtils

class WondersAdapter(
    private val context: Context,
    private val wonders: List<Wonder>,
    private val resourceProvider: ResourceProvider,
    private val onWonderClickCallback: OnWonderClickCallback,
) : Adapter<WondersAdapter.WonderHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WonderHolder {
        val binding = ItemCardWonderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return WonderHolder(binding)
    }

    override fun getItemCount(): Int {
        return wonders.size
    }

    override fun onBindViewHolder(holder: WonderHolder, position: Int) {
        holder.bind(
            context, wonders[position], resourceProvider, onWonderClickCallback
        )
    }

    class WonderHolder(private val itemBinding: ItemCardWonderBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(
            context: Context,
            wonder: Wonder,
            resourceProvider: ResourceProvider,
            onWonderClickCallback: OnWonderClickCallback,
        ) {
            itemBinding.ivWonderCover.setImageDrawable(
                resourceProvider.getDrawable(wonder.cover)
            )

            itemBinding.mcvWonderCard.setOnClickListener {
                onWonderClickCallback.onWonderClick(wonder)
            }

            itemBinding.mcvWonderCard.setOnLongClickListener {
                ToastUtils.showToast(context, wonder.title)
                true
            }
        }
    }
}