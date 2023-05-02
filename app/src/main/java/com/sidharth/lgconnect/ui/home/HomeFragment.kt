package com.sidharth.lgconnect.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.sidharth.lgconnect.data.repository.HomeRepositoryImpl
import com.sidharth.lgconnect.databinding.FragmentHomeBinding
import com.sidharth.lgconnect.domain.usecase.DeleteMarkerUseCaseImpl
import com.sidharth.lgconnect.domain.usecase.GetHomeDataUseCaseImpl
import com.sidharth.lgconnect.domain.usecase.GetMarkersUseCaseImpl
import com.sidharth.lgconnect.ui.home.adapter.ChartsAdapter
import com.sidharth.lgconnect.ui.home.adapter.MarkersAdapter
import com.sidharth.lgconnect.ui.home.adapter.PlanetAdapter
import com.sidharth.lgconnect.ui.home.adapter.WondersAdapter
import com.sidharth.lgconnect.ui.home.viewmodel.HomeViewModel
import com.sidharth.lgconnect.ui.home.viewmodel.HomeViewModelFactory
import com.sidharth.lgconnect.util.ResourceProvider

class HomeFragment : Fragment() {
    private lateinit var resourceProvider: ResourceProvider
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            GetHomeDataUseCaseImpl(HomeRepositoryImpl()),
            GetMarkersUseCaseImpl(HomeRepositoryImpl()),
            DeleteMarkerUseCaseImpl(HomeRepositoryImpl())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resourceProvider = ResourceProvider(requireContext())
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding: FragmentHomeBinding = FragmentHomeBinding.inflate(inflater)

        binding.rvPlanets.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false
        )
        binding.rvPlanets.adapter = context?.let {
            PlanetAdapter(
                context = it,
                planets = viewModel.homeData.planets,
                resourceProvider = resourceProvider,
                lifecycleScope = lifecycleScope
            )
        }
        LinearSnapHelper().attachToRecyclerView(binding.rvPlanets)

        binding.rvCharts.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false
        )
        binding.rvCharts.adapter = context?.let {
            ChartsAdapter(
                context = it,
                charts = viewModel.homeData.charts,
                resourceProvider = resourceProvider,
                lifecycleScope = lifecycleScope
            )
        }
        LinearSnapHelper().attachToRecyclerView(binding.rvCharts)

        binding.rvWonders.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false
        )
        binding.rvWonders.adapter = context?.let {
            WondersAdapter(
                context = it,
                wonders = viewModel.homeData.wonders,
                resourceProvider = resourceProvider,
                lifecycleScope = lifecycleScope
            )
        }
        LinearSnapHelper().attachToRecyclerView(binding.rvWonders)

        binding.rvMarkers.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )

        binding.rvMarkers.adapter = context?.let { ctx ->
            viewModel.markers.value?.let {
                MarkersAdapter(
                    context = ctx,
                    markers = it,
                    resourceProvider = resourceProvider,
                    lifecycleScope = lifecycleScope
                )
            }
        }
        LinearSnapHelper().attachToRecyclerView(binding.rvMarkers)

        viewModel.markers.observe(viewLifecycleOwner) {
            binding.rvMarkers.adapter = context?.let { ctx ->
                MarkersAdapter(
                    context = ctx,
                    markers = it,
                    resourceProvider = resourceProvider,
                    lifecycleScope = lifecycleScope
                )
            }
            binding.rvMarkers.adapter?.notifyDataSetChanged()
        }

        return binding.root
    }

}