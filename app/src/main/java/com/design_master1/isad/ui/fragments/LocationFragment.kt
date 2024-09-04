package com.design_master1.isad.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.design_master1.isad.R
import com.design_master1.isad.adapter.ImagesAdapter
import com.design_master1.isad.databinding.FragmentLocationBinding
import com.design_master1.isad.model.view_models.activities.MainActivityViewModel
import com.design_master1.isad.model.view_models.fragments.LocationFragmentViewModel
import com.design_master1.isad.model.view_models.fragments.LocationFragmentViewModel.FetchLocationState
import com.design_master1.isad.ui.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LocationFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mBinding: FragmentLocationBinding
    private val mMainActivityViewModel: MainActivityViewModel by activityViewModels()
    private val mViewModel: LocationFragmentViewModel by viewModels()
    private lateinit var mMainActivity: MainActivity
    private val mImagesAdapter = ImagesAdapter()
    private var mMarker: Marker? = null
    private var mapView: View? = null
    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentLocationBinding.inflate(layoutInflater)

        mMainActivity = requireActivity() as MainActivity

        mMainActivity.setActionBar(shouldShowActionBar = false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapView = mapFragment.view
        mapFragment.getMapAsync(this)

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mBinding.recyclerImages.layoutManager = layoutManager
        mBinding.recyclerImages.addItemDecoration(DividerItemDecoration(requireContext(), layoutManager.orientation))
        mBinding.recyclerImages.adapter = mImagesAdapter

        mViewModel.fetchLocationState.observe(viewLifecycleOwner){
            mBinding.shimmerParent.visibility = if (it == FetchLocationState.FETCHING) View.VISIBLE else View.GONE
            mBinding.normalParent.visibility = if (it == FetchLocationState.FETCHING) View.GONE else View.VISIBLE

            if (it == FetchLocationState.FAILURE){
                mMainActivity.showToast(getString(R.string.something_went_wrong_try_again))
                findNavController().popBackStack()
            }

            if (it == FetchLocationState.FETCHED){
                mViewModel.locationData?.let {
                    addMarker(latLng = LatLng(it.latitude.toDouble(), it.longitude.toDouble()),shouldAnimate = true, zoom = 10f)
                    mBinding.description.text = it.description

//                    Glide.with(requireContext())
//                        .load("${mViewModel.mElectroLibAccessProvider.getServerBaseUrl()})
//                        .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(16)))
//                        .into(mBinding.image)

                    mImagesAdapter.setData(mViewModel.images)
                }

            }
        }

        mBinding.direction.setOnClickListener {
            goToGoogleMap()
        }

        return mBinding.root
    }
    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
    }
    private fun animateMap(latLng: LatLng, zoom: Float = mMap.cameraPosition.zoom) {
        val cameraPosition =
            CameraPosition.Builder().target(latLng)
                .zoom(zoom)
                .build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
    private fun addMarker(latLng: LatLng, shouldAnimate: Boolean = false, zoom: Float = mMap.cameraPosition.zoom){
        val markerOptions = MarkerOptions()
        markerOptions.position(LatLng(latLng.latitude,latLng.longitude))
        mMarker?.remove()
        mMarker = mMap.addMarker(markerOptions)
        if (shouldAnimate) animateMap(latLng,zoom)
    }
    private fun goToGoogleMap(){
//        29.38100635480117,47.99112702521263

        try {
            val gmmIntentUri =
                Uri.parse("google.navigation:q=29.38100635480117,47.99112702521263&mode=d")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        } catch (e: Exception){
            Log.d(TAG, "${e.message}  ${e.printStackTrace()}")
        }

    }

    override fun onResume() {
        super.onResume()
        if (mViewModel.locationData == null)
            mViewModel.fetchLocation()
    }
    companion object{
        private const val TAG = "LocationFragment"
    }
}