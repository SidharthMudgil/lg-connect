package com.sidharth.lgconnect.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.sidharth.lgconnect.R
import com.sidharth.lgconnect.data.repository.AppRepository
import com.sidharth.lgconnect.databinding.FragmentHomeBinding
import com.sidharth.lgconnect.domain.usecase.AddObserverUseCaseImpl
import com.sidharth.lgconnect.domain.usecase.DeleteMarkerUseCaseImpl
import com.sidharth.lgconnect.domain.usecase.GetHomeDataUseCaseImpl
import com.sidharth.lgconnect.domain.usecase.GetMarkersUseCaseImpl
import com.sidharth.lgconnect.ui.home.adapter.ChartsAdapter
import com.sidharth.lgconnect.ui.home.adapter.MarkersAdapter
import com.sidharth.lgconnect.ui.home.adapter.PlanetAdapter
import com.sidharth.lgconnect.ui.home.adapter.WondersAdapter
import com.sidharth.lgconnect.ui.home.callback.OnItemClickCallback
import com.sidharth.lgconnect.ui.home.callback.SwipeToDeleteCallback
import com.sidharth.lgconnect.ui.home.viewmodel.HomeViewModel
import com.sidharth.lgconnect.ui.home.viewmodel.HomeViewModelFactory
import com.sidharth.lgconnect.ui.viewmodel.ConnectionStatusViewModel
import com.sidharth.lgconnect.util.DialogUtils
import com.sidharth.lgconnect.util.ResourceProvider
import com.sidharth.lgconnect.util.ToastUtils

class HomeFragment : Fragment(), OnItemClickCallback {
    private lateinit var resourceProvider: ResourceProvider
    private val viewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            GetHomeDataUseCaseImpl(AppRepository.getInstance(requireContext())),
            GetMarkersUseCaseImpl(
                AppRepository.getInstance(requireContext())
            ),
            DeleteMarkerUseCaseImpl(
                AppRepository.getInstance(requireContext())
            ),
            AddObserverUseCaseImpl(
                AppRepository.getInstance(requireContext())
            )
        )
    }
    private val connectionStatusViewModel: ConnectionStatusViewModel by activityViewModels()
    private lateinit var dialog: DialogUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resourceProvider = ResourceProvider(requireContext())
        dialog = DialogUtils(context = requireContext(),
            image = resourceProvider.getDrawable(R.drawable.cartoon3),
            title = resourceProvider.getString(R.string.no_connection_title),
            description = resourceProvider.getString(R.string.no_connection_description),
            buttonLabel = resourceProvider.getString(R.string.no_connection_button_text),
            onDialogButtonClick = { dialog.dismiss() })
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding: FragmentHomeBinding = FragmentHomeBinding.inflate(inflater)

        binding.searchLayout.mcvSearch.setOnClickListener {
            ToastUtils.showToast(requireContext(), "Coming soon...")
        }

        binding.rvPlanets.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false
        )
        binding.rvPlanets.adapter = context?.let {
            PlanetAdapter(
                context = it,
                planets = viewModel.homeData.planets,
                resourceProvider = resourceProvider,
                lifecycleScope = lifecycleScope,
                onItemClickCallback = this,
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
                lifecycleScope = lifecycleScope,
                onItemClickCallback = this,
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
                lifecycleScope = lifecycleScope,
                onItemClickCallback = this,
            )
        }
        LinearSnapHelper().attachToRecyclerView(binding.rvWonders)

        binding.rvMarkers.visibility = View.GONE
        binding.label.visibility = View.GONE
        viewModel.markers.observe(viewLifecycleOwner) { markers ->
            if (markers.isEmpty()) {
                binding.rvMarkers.visibility = View.GONE
                binding.label.visibility = View.GONE
            } else {
                binding.rvMarkers.visibility = View.VISIBLE
                binding.label.visibility = View.VISIBLE

                binding.rvMarkers.adapter = context?.let { ctx ->
                    MarkersAdapter(
                        context = ctx,
                        markers = markers,
                        resourceProvider = resourceProvider,
                        lifecycleScope = lifecycleScope,
                        onItemClickCallback = this,
                    )
                }
                binding.rvMarkers.adapter?.notifyDataSetChanged()
            }
        }
        binding.rvMarkers.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.markers.value?.get(viewHolder.adapterPosition)
                    ?.let {
                        ToastUtils.showToast(requireContext(), "${it.title} deleted")
                        viewModel.deleteMarker(it)
                    }
                binding.rvMarkers.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
            }

        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rvMarkers)
        LinearSnapHelper().attachToRecyclerView(binding.rvMarkers)

        return binding.root
    }

    override fun onClick(action: () -> Unit) {
        when (connectionStatusViewModel.connectionStatus.value) {
            true -> action()
            false -> dialog.show()
            else -> dialog.show()
        }
    }
}