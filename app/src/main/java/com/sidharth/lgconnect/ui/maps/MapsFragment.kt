package com.sidharth.lgconnect.ui.maps

import NetworkUtils
import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.sidharth.lgconnect.R
import com.sidharth.lgconnect.data.mapper.MarkerMapper
import com.sidharth.lgconnect.data.repository.DataRepositoryImpl
import com.sidharth.lgconnect.domain.usecase.AddMarkerUseCaseImpl
import com.sidharth.lgconnect.domain.usecase.AddObserverUseCaseImpl
import com.sidharth.lgconnect.domain.usecase.GetMarkersUseCaseImpl
import com.sidharth.lgconnect.ui.maps.viewmodel.MapsViewModel
import com.sidharth.lgconnect.ui.maps.viewmodel.MapsViewModelFactory


class MapsFragment : Fragment() {
    private val viewModel: MapsViewModel by viewModels {
        MapsViewModelFactory(
            GetMarkersUseCaseImpl(DataRepositoryImpl),
            AddMarkerUseCaseImpl(DataRepositoryImpl),
            AddObserverUseCaseImpl(DataRepositoryImpl)
        )
    }

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

        viewModel.markers.observe(viewLifecycleOwner) { markers ->
            googleMap.clear()

            markers.forEach { mkr ->
                googleMap.addMarker(
                    MarkerOptions().position(mkr.latLng).title(mkr.title)
                )
            }
        }

        val vibrator = context?.getSystemService(Vibrator::class.java)

        googleMap.setOnMapLongClickListener { latLng ->
            context?.let { ctx ->
                if (NetworkUtils.isNetworkConnected(ctx)) {
                    val marker =
                        MarkerMapper(ctx).mapAddressToMarker(latLng.latitude, latLng.longitude)

                    marker?.let {
                        vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
                        viewModel.addMarker(it)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        NetworkUtils.startNetworkCallback(context = requireContext(), onConnectionLost = {
            NetworkUtils.showNoNetworkDialog(
                context = requireContext(),
                onRetry = { onRetry(requireContext()) },
            )
        }, onConnectionAvailable = { NetworkUtils.dismissNoNetworkDialog() })
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun onRetry(context: Context) {
        if (NetworkUtils.isNetworkConnected(context)) {
            NetworkUtils.dismissNoNetworkDialog()
        } else {
            NetworkUtils.showNoNetworkDialog(context = context, onRetry = {
                onRetry(context)
            })
        }
    }
}