package com.sidharth.lgconnect.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.sidharth.lgconnect.domain.model.Wonder
import com.sidharth.lgconnect.databinding.ItemCardWonderBinding
import com.sidharth.lgconnect.service.ServiceManager
import com.sidharth.lgconnect.util.DialogUtils
import com.sidharth.lgconnect.util.ResourceProvider
import com.sidharth.lgconnect.util.ToastUtils
import kotlinx.coroutines.launch

class WondersAdapter(
    private val context: Context,
    private val wonders: List<Wonder>,
    private val resourceProvider: ResourceProvider,
    private val lifecycleScope: LifecycleCoroutineScope,
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
        holder.bind(context, wonders[position], resourceProvider, lifecycleScope)
    }

    class WonderHolder(private val itemBinding: ItemCardWonderBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(
            context: Context,
            wonder: Wonder,
            resourceProvider: ResourceProvider,
            lifecycleScope: LifecycleCoroutineScope
        ) {
            itemBinding.ivWonderCover.setImageDrawable(
                resourceProvider.getDrawable(wonder.cover)
            )

            itemBinding.mcvWonderCard.setOnClickListener {
//                lifecycleScope.launch {
//                    ServiceManager.getLGService()?.flyToAndOrbit(wonder.latLng)
//                        ?: DialogUtils.show(context) {
//
//                        }
//                }
            }

            itemBinding.mcvWonderCard.setOnLongClickListener {
                ToastUtils.showToast(context, wonder.title)
                true
            }
        }
    }
}