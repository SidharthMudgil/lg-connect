package com.sidharth.lgconnect.ui.maps

import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
import com.sidharth.lgconnect.util.DialogUtils
import com.sidharth.lgconnect.util.NetworkUtils


class MapsFragment : Fragment() {
    private val viewModel: MapsViewModel by viewModels {
        MapsViewModelFactory(
            GetMarkersUseCaseImpl(DataRepositoryImpl),
            AddMarkerUseCaseImpl(DataRepositoryImpl),
            AddObserverUseCaseImpl(DataRepositoryImpl)
        )
    }
    private lateinit var dialog: DialogUtils

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

                    marker?.let { mkr ->
                        vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
                        viewModel.addMarker(mkr)
                    }
                } else {
                    dialog.show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        dialog = DialogUtils(
            context = requireContext(),
            image = ContextCompat.getDrawable(requireContext(), R.drawable.cartoon1)!!,
            title = getString(R.string.no_network_title),
            description = getString(R.string.no_network_description),
            buttonLabel = getString(R.string.no_network_button_text),
            showButton = false
        )

        NetworkUtils.startNetworkCallback(context = requireContext(), onConnectionLost = {
            dialog.show()
        }, onConnectionAvailable = {
            dialog.dismiss()
        })

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}