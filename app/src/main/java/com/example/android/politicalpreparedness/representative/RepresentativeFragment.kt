package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.util.*

class DetailFragment : Fragment() {

    //DONE: Add Constant for Location request
    companion object {
        private const val REQUEST_ACCESS_FINE_LOCATION = 101
    }

    private val viewModel: RepresentativeViewModel by lazy {
        ViewModelProvider(this)[RepresentativeViewModel::class.java]
    }

    lateinit var binding: FragmentRepresentativeBinding

    //DONE: Declare ViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //DONE: Establish bindings
        binding = FragmentRepresentativeBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.rvRepresentatives.adapter = RepresentativeListAdapter()

        //DONE: Define and assign Representative adapter
        savedInstanceState?.let {
            it.getInt("motion_layout_state").let { restoredState ->
                binding.motionLayout.transitionToState(restoredState)
            }
        }

        //DONE: Populate Representative adapter

        //DONE: Establish button listeners for field and location search
        binding.buttonSearch.setOnClickListener {
            hideKeyboard()
            viewModel.getRepresentativesList()
        }
        binding.buttonLocation.setOnClickListener {
            hideKeyboard()
            getRepresentativeWithPermissionHandling()
        }

        return binding.root

    }

    @Suppress("DEPRECATION")
    private fun getRepresentativeWithPermissionHandling() {
        return if (isPermissionGranted()) {
            getLocation()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        //DONE: Handle location permission result to get location on permission granted
        if (REQUEST_ACCESS_FINE_LOCATION == requestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                Snackbar.make(
                    requireView(),
                    R.string.error_require_location_permission,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun checkLocationPermissions(): Boolean {
        return isPermissionGranted()
    }

    //DONE: Check if permission is already granted and return (true = granted, false = denied/other)

    private fun isPermissionGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {

        //DONE: Get location from LocationServices
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        //DONE: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
        fusedLocationClient.lastLocation

            .addOnSuccessListener { location ->
                location?.let {
                    val address = geoCodeLocation(location)
                    Timber.d("address: %s", address)
                    viewModel.getRepresentativesList(address)
                }
            }

            .addOnFailureListener { e ->
                Timber.e("Error: %s", e.localizedMessage)
            }
    }


    @Suppress("DEPRECATION")
    private fun geoCodeLocation(location: Location): Address {

        val geocoder = Geocoder(requireContext(), Locale.getDefault())

        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            ?.map { address ->
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
            ?.first() ?: Address(
            "",
            "",
            "",
            "",
            ""
        )

    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}
