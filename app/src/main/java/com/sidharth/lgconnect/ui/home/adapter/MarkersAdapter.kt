package com.sidharth.lgconnect.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.sidharth.lgconnect.databinding.ItemCardMarkerBinding
import com.sidharth.lgconnect.domain.model.Marker
import com.sidharth.lgconnect.service.ServiceManager
import com.sidharth.lgconnect.ui.home.callback.OnItemClickCallback
import com.sidharth.lgconnect.util.ResourceProvider
import com.sidharth.lgconnect.util.ToastUtils
import kotlinx.coroutines.launch

class MarkersAdapter(
    private val context: Context,
    private val markers: List<Marker>,
    private val resourceProvider: ResourceProvider,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val onItemClickCallback: OnItemClickCallback,
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
        holder.bind(
            context,
            markers[position],
            resourceProvider,
            lifecycleScope,
            onItemClickCallback
        )
    }

    class MarkerHolder(private val itemBinding: ItemCardMarkerBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(
            context: Context,
            marker: Marker,
            resourceProvider: ResourceProvider,
            lifecycleScope: LifecycleCoroutineScope,
            onItemClickCallback: OnItemClickCallback,
        ) {
            itemBinding.sivMarkerIcon.setImageDrawable(
                resourceProvider.getDrawable(marker.icon)
            )

            itemBinding.tvMarkerTitle.text = marker.title
            itemBinding.tvMarkerSubtitle.text = marker.subtitle

            itemBinding.mcvMarkerCard.setOnClickListener {
                onItemClickCallback.onClick {
                    lifecycleScope.launch {
                        ServiceManager.getLGService()?.createMarker(marker)
                    }
                }
            }

            itemBinding.mcvMarkerCard.setOnLongClickListener {
                ToastUtils.showToast(context, marker.title)
                true
            }
        }
    }
}