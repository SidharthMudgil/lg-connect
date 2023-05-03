package com.sidharth.lgconnect.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.sidharth.lgconnect.domain.model.Chart
import com.sidharth.lgconnect.databinding.ItemCardChartBinding
import com.sidharth.lgconnect.service.ServiceManager
import com.sidharth.lgconnect.util.DialogUtils
import com.sidharth.lgconnect.util.ResourceProvider
import com.sidharth.lgconnect.util.ToastUtils
import kotlinx.coroutines.launch

class ChartsAdapter(
    private val context: Context,
    private val charts: List<Chart>,
    private val resourceProvider: ResourceProvider,
    private val lifecycleScope: LifecycleCoroutineScope,
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
        holder.bind(context, charts[position], resourceProvider, lifecycleScope)
    }

    class ChartHolder(private val itemBinding: ItemCardChartBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(
            context: Context,
            chart: Chart,
            resourceProvider: ResourceProvider,
            lifecycleScope: LifecycleCoroutineScope
        ) {
            itemBinding.ivChartCover.setImageDrawable(
                resourceProvider.getDrawable(chart.cover)
            )

            itemBinding.mcvChartCard.setOnClickListener {
//                lifecycleScope.launch {
//                    ServiceManager.getLGService()?.createChart(chart.type)
//                        ?: DialogUtils.show(context) {
//
//                        }
//                }
            }

            itemBinding.mcvChartCard.setOnLongClickListener {
                ToastUtils.showToast(context, chart.type)
                true
            }
        }
    }
}