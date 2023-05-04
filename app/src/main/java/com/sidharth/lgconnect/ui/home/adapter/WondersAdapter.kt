package com.sidharth.lgconnect.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.sidharth.lgconnect.databinding.ItemCardWonderBinding
import com.sidharth.lgconnect.domain.model.Wonder
import com.sidharth.lgconnect.service.ServiceManager
import com.sidharth.lgconnect.ui.home.callback.OnItemClickCallback
import com.sidharth.lgconnect.util.ResourceProvider
import com.sidharth.lgconnect.util.ToastUtils
import kotlinx.coroutines.launch

class WondersAdapter(
    private val context: Context,
    private val wonders: List<Wonder>,
    private val resourceProvider: ResourceProvider,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val onItemClickCallback: OnItemClickCallback,
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
        holder.bind(
            context,
            wonders[position],
            resourceProvider,
            lifecycleScope,
            onItemClickCallback
        )
    }

    class WonderHolder(private val itemBinding: ItemCardWonderBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(
            context: Context,
            wonder: Wonder,
            resourceProvider: ResourceProvider,
            lifecycleScope: LifecycleCoroutineScope,
            onItemClickCallback: OnItemClickCallback,
        ) {
            itemBinding.ivWonderCover.setImageDrawable(
                resourceProvider.getDrawable(wonder.cover)
            )

            itemBinding.mcvWonderCard.setOnClickListener {
                onItemClickCallback.onClick {
                    lifecycleScope.launch {
                        ServiceManager.getLGService()?.flyToAndOrbit(wonder.latLng)
                            ?: ServiceManager.showNoConnectionDialog()
                    }
                }
            }

            itemBinding.mcvWonderCard.setOnLongClickListener {
                ToastUtils.showToast(context, wonder.title)
                true
            }
        }
    }
}