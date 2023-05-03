package com.sidharth.lgconnect.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.sidharth.lgconnect.domain.model.Marker
import com.sidharth.lgconnect.databinding.ItemCardMarkerBinding
import com.sidharth.lgconnect.service.ServiceManager
import com.sidharth.lgconnect.util.DialogUtils
import com.sidharth.lgconnect.util.ResourceProvider
import com.sidharth.lgconnect.util.ToastUtils
import kotlinx.coroutines.launch

class MarkersAdapter(
    private val context: Context,
    private val markers: List<Marker>,
    private val resourceProvider: ResourceProvider,
    private val lifecycleScope: LifecycleCoroutineScope,
) : RecyclerView.Adapter<MarkersAdapter.MarkerHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerHolder {
        val binding = ItemCardMarkerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MarkerHolder(binding)
    }

    override fun getItemCount(): Int {
        return markers.size
    }

    override fun onBindViewHolder(holder: MarkerHolder, position: Int) {
        holder.bind(context, markers[position], resourceProvider, lifecycleScope)
    }

    class MarkerHolder(private val itemBinding: ItemCardMarkerBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(
            context: Context,
            marker: Marker,
            resourceProvider: ResourceProvider,
            lifecycleScope: LifecycleCoroutineScope
        ) {
            itemBinding.sivMarkerIcon.setImageDrawable(
                resourceProvider.getDrawable(marker.icon)
            )

            itemBinding.tvMarkerTitle.text = marker.title
            itemBinding.tvMarkerSubtitle.text = marker.subtitle

            itemBinding.mcvMarkerCard.setOnClickListener {
//                lifecycleScope.launch {
//                    ServiceManager.getLGService()?.createMarker(marker) ?: DialogUtils.show(context) {
//
//                    }
//                }
            }

            itemBinding.mcvMarkerCard.setOnLongClickListener {
                ToastUtils.showToast(context, marker.title)
                true
            }
        }
    }
}