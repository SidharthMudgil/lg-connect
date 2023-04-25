package com.sidharth.lgconnect.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.sidharth.lgconnect.DummyData
import com.sidharth.lgconnect.databinding.FragmentHomeBinding
import com.sidharth.lgconnect.ui.home.adapter.ChartsAdapter
import com.sidharth.lgconnect.ui.home.adapter.MarkersAdapter
import com.sidharth.lgconnect.ui.home.adapter.PlanetAdapter
import com.sidharth.lgconnect.ui.home.adapter.WondersAdapter
import com.sidharth.lgconnect.util.ResourceProvider

class HomeFragment : Fragment() {
    private lateinit var resourceProvider: ResourceProvider
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resourceProvider = ResourceProvider(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentHomeBinding = FragmentHomeBinding.inflate(inflater)

        binding.rvPlanets.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false
        )
        binding.rvPlanets.adapter = context?.let {
            PlanetAdapter(
                context = it,
                planets = DummyData.planets,
                resourceProvider = resourceProvider
            )
        }
        LinearSnapHelper().attachToRecyclerView(binding.rvPlanets)

        binding.rvCharts.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false
        )
        binding.rvCharts.adapter = context?.let {
            ChartsAdapter(
                context = it,
                charts = DummyData.charts,
                resourceProvider = resourceProvider
            )
        }
        LinearSnapHelper().attachToRecyclerView(binding.rvCharts)

        binding.rvWonders.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false
        )
        binding.rvWonders.adapter = context?.let {
            WondersAdapter(
                context = it,
                wonders = DummyData.wonders,
                resourceProvider = resourceProvider
            )
        }
        LinearSnapHelper().attachToRecyclerView(binding.rvWonders)

        binding.rvMarkers.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        binding.rvMarkers.adapter = context?.let {
            MarkersAdapter(
                context = it,
                markers = DummyData.markers,
                resourceProvider = resourceProvider
            )
        }
        LinearSnapHelper().attachToRecyclerView(binding.rvMarkers)

        return binding.root
    }

}