package com.sidharth.lgconnect.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sidharth.lgconnect.domain.model.Chart
import com.sidharth.lgconnect.databinding.ItemCardChartBinding
import com.sidharth.lgconnect.util.ResourceProvider
import com.sidharth.lgconnect.util.ToastUtils

class ChartsAdapter(
    private val context: Context,
    private val charts: List<Chart>,
    private val resourceProvider: ResourceProvider
) :
    RecyclerView.Adapter<ChartsAdapter.ChartHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartHolder {
        val binding = ItemCardChartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return ChartHolder(binding)
    }

    override fun getItemCount(): Int {
        return charts.size
    }

    override fun onBindViewHolder(holder: ChartHolder, position: Int) {
        holder.bind(context, charts[position], resourceProvider)
    }

    class ChartHolder(private val itemBinding: ItemCardChartBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(context: Context, chart: Chart, resourceProvider: ResourceProvider) {
            itemBinding.ivChartCover.setImageDrawable(
                resourceProvider.getDrawable(chart.cover)
            )

            itemBinding.mcvChartCard.setOnClickListener {

            }

            itemBinding.mcvChartCard.setOnLongClickListener {
                ToastUtils.showToast(context, chart.type)
                true
            }
        }
    }
}