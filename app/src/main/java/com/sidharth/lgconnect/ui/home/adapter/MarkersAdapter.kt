package com.sidharth.lgconnect.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sidharth.lgconnect.domain.model.Marker
import com.sidharth.lgconnect.databinding.ItemCardMarkerBinding
import com.sidharth.lgconnect.util.ResourceProvider
import com.sidharth.lgconnect.util.ToastUtils

class MarkersAdapter(
    private val context: Context,
    private val markers: List<Marker>,
    private val resourceProvider: ResourceProvider
) :
    RecyclerView.Adapter<MarkersAdapter.MarkerHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerHolder {
        val binding = ItemCardMarkerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return MarkerHolder(binding)
    }

    override fun getItemCount(): Int {
        return markers.size
    }

    override fun onBindViewHolder(holder: MarkerHolder, position: Int) {
        holder.bind(context, markers[position], resourceProvider)
    }

    class MarkerHolder(private val itemBinding: ItemCardMarkerBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(context: Context, marker: Marker, resourceProvider: ResourceProvider) {
            itemBinding.sivMarkerIcon.setImageDrawable(
                resourceProvider.getDrawable(marker.icon)
            )

            itemBinding.tvMarkerTitle.text = marker.title
            itemBinding.tvMarkerSubtitle.text = marker.subtitle

            itemBinding.mcvMarkerCard.setOnClickListener {

            }

            itemBinding.mcvMarkerCard.setOnLongClickListener {
                ToastUtils.showToast(context, marker.title)
                true
            }
        }
    }
}