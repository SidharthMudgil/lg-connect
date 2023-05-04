package com.sidharth.lgconnect.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sidharth.lgconnect.databinding.ItemCardPlanetBinding
import com.sidharth.lgconnect.domain.model.Planet
import com.sidharth.lgconnect.service.ServiceManager
import com.sidharth.lgconnect.ui.home.callback.OnItemClickCallback
import com.sidharth.lgconnect.util.ResourceProvider
import com.sidharth.lgconnect.util.ToastUtils
import kotlinx.coroutines.launch

class PlanetAdapter(
    private val context: Context,
    private val planets: List<Planet>,
    private val resourceProvider: ResourceProvider,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val onItemClickCallback: OnItemClickCallback,
) : Adapter<PlanetAdapter.PlanetHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanetHolder {
        val binding = ItemCardPlanetBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return PlanetHolder(binding)
    }

    override fun getItemCount(): Int {
        return planets.size
    }

    override fun onBindViewHolder(holder: PlanetHolder, position: Int) {
        holder.bind(
            context,
            planets[position],
            resourceProvider,
            lifecycleScope,
            onItemClickCallback
        )
    }

    class PlanetHolder(private val itemBinding: ItemCardPlanetBinding) :
        ViewHolder(itemBinding.root) {
        fun bind(
            context: Context,
            planet: Planet,
            resourceProvider: ResourceProvider,
            lifecycleScope: LifecycleCoroutineScope,
            onItemClickCallback: OnItemClickCallback,
        ) {
            itemBinding.ivPlanetCover.setImageDrawable(
                resourceProvider.getDrawable(planet.cover)
            )

            itemBinding.mcvPlanetCard.setOnClickListener {
                onItemClickCallback.onClick {
                    lifecycleScope.launch {
                        ServiceManager.getLGService()?.changePlanet(planet.name)
                    }
                }
            }

            itemBinding.mcvPlanetCard.setOnLongClickListener {
                ToastUtils.showToast(context, planet.name)
                true
            }
        }
    }
}