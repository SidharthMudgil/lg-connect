package com.sidharth.lgconnect.ui.maps

import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.sidharth.lgconnect.R
import com.sidharth.lgconnect.data.mapper.MarkerMapper
import com.sidharth.lgconnect.data.repository.AppRepository
import com.sidharth.lgconnect.domain.usecase.AddMarkerUseCaseImpl
import com.sidharth.lgconnect.domain.usecase.AddObserverUseCaseImpl
import com.sidharth.lgconnect.domain.usecase.GetMarkersUseCaseImpl
import com.sidharth.lgconnect.ui.maps.viewmodel.MapsViewModel
import com.sidharth.lgconnect.ui.maps.viewmodel.MapsViewModelFactory
import com.sidharth.lgconnect.util.LGManager
import com.sidharth.lgconnect.util.NetworkUtils
import com.sidharth.lgconnect.util.ToastUtils
import kotlinx.coroutines.launch


class MapsFragment : Fragment(), OnMapLongClickListener {
    private val viewModel: MapsViewModel by activityViewModels {
        MapsViewModelFactory(
            GetMarkersUseCaseImpl(
                AppRepository.getInstance(requireContext())
            ),
            AddMarkerUseCaseImpl(
                AppRepository.getInstance(requireContext())
            ),
            AddObserverUseCaseImpl(
                AppRepository.getInstance(requireContext())
            )
        )
    }
    private lateinit var vibrator: Vibrator

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.setMapStyle(context?.let {
            MapStyleOptions.loadRawResourceStyle(
                it, R.raw.map_style_dark
            )
        })

        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(21.51, 81.23), 1f
            )
        )

        googleMap.setOnCameraIdleListener {
            updateMap(googleMap.cameraPosition)
        }

        viewModel.markers.observe(viewLifecycleOwner) { markers ->
            googleMap.clear()
            markers.forEach { mkr ->
                googleMap.addMarker(
                    MarkerOptions().position(mkr.latLng).title(mkr.title)
                )
            }
        }

        googleMap.setOnMapLongClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        vibrator = requireContext().getSystemService(Vibrator::class.java)

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun updateMap(cameraPosition: CameraPosition) {
        if (NetworkUtils.isNetworkConnected(requireContext())) {
            if (LGManager.getInstance()?.connected == true) {
                lifecycleScope.launch {
                    LGManager.getInstance()?.flyTo(cameraPosition)
                }
            }
        }
    }

    override fun onMapLongClick(latLng: LatLng) {
        if (NetworkUtils.isNetworkConnected(requireContext())) {
            val marker = MarkerMapper.mapAddressToMarker(
                context = requireContext(),
                latitude = latLng.latitude,
                longitude = latLng.longitude
            )
            marker?.let { mkr ->
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
                ToastUtils.showToast(requireContext(), "${mkr.title} added")
                viewModel.addMarker(mkr)
            }
        } else {
            ToastUtils.showToast(requireContext(), "No Network Connection")
        }
    }
}