package com.sidharth.lgconnect

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sidharth.lgconnect.databinding.ItemCardMarkerBinding

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
        holder.bind(markers[position], resourceProvider)
    }

    class MarkerHolder(private val itemBinding: ItemCardMarkerBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(marker: Marker, resourceProvider: ResourceProvider) {
            itemBinding.sivMarkerIcon.setImageDrawable(
                resourceProvider.getDrawable(marker.icon)
            )

            itemBinding.tvMarkerTitle.text = marker.title
            itemBinding.tvMarkerSubtitle.text = marker.subtitle

            itemBinding.mcvMarkerCard.setOnClickListener {

            }
        }
    }
}