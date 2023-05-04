package com.sidharth.lgconnect.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.sidharth.lgconnect.databinding.ItemCardChartBinding
import com.sidharth.lgconnect.domain.model.Chart
import com.sidharth.lgconnect.service.ServiceManager
import com.sidharth.lgconnect.ui.home.callback.OnItemClickCallback
import com.sidharth.lgconnect.util.ResourceProvider
import com.sidharth.lgconnect.util.ToastUtils
import kotlinx.coroutines.launch

class ChartsAdapter(
    private val context: Context,
    private val charts: List<Chart>,
    private val resourceProvider: ResourceProvider,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val onItemClickCallback: OnItemClickCallback,
) : RecyclerView.Adapter<ChartsAdapter.ChartHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartHolder {
        val binding = ItemCardChartBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ChartHolder(binding)
    }

    override fun getItemCount(): Int {
        return charts.size
    }

    override fun onBindViewHolder(holder: ChartHolder, position: Int) {
        holder.bind(
            context, charts[position], resourceProvider, lifecycleScope, onItemClickCallback
        )
    }

    class ChartHolder(private val itemBinding: ItemCardChartBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(
            context: Context,
            chart: Chart,
            resourceProvider: ResourceProvider,
            lifecycleScope: LifecycleCoroutineScope,
            onItemClickCallback: OnItemClickCallback,
        ) {
            itemBinding.ivChartCover.setImageDrawable(
                resourceProvider.getDrawable(chart.cover)
            )

            itemBinding.mcvChartCard.setOnClickListener {
                onItemClickCallback.onClick {
                    lifecycleScope.launch {
                        ServiceManager.getLGService()?.createChart(chart.type)
                            ?: ServiceManager.showNoConnectionDialog()
                    }
                }
            }

            itemBinding.mcvChartCard.setOnLongClickListener {
                ToastUtils.showToast(context, chart.type)
                true
            }
        }
    }
}