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

    private var _binding: FragmentRepresentativeBinding? = null
    private val binding get() = _binding!!

    //DONE: Add Constant for Location request
    companion object {
        private const val REQUEST_ACCESS_FINE_LOCATION = 101
    }

    // DONE: Declare ViewModel
    private val viewModel: RepresentativeViewModel by lazy {

        // Establish RepresentativeViewModel with ViewModelProvider
        ViewModelProvider(this)[RepresentativeViewModel::class.java]

    }

    /**
     * Inflates the layout with Data Binding, sets its lifecycle owner to the OverviewFragment to
     * enable Data Binding to observe LiveData, and sets up RecyclerView with an adapter.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //DONE: Establish bindings
        _binding = FragmentRepresentativeBinding.inflate(inflater, container, false)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = viewLifecycleOwner

        // Bind ViewModel
        binding.viewModel = viewModel

        //DONE: Populate Representative adapter
        // Assigns the RepresentativeListAdapter to the RecyclerView
        binding.rvRepresentatives.adapter = RepresentativeListAdapter()

        //DONE: Define and assign Representative adapter
        savedInstanceState?.let {

            // Restore the state of the ViewModel
            it.getInt("motion_layout_state").let { restoredState ->

                // .transitionToState() is used to restore the state of the MotionLayout
                binding.motionLayout.transitionToState(restoredState)

            }

        }

        //DONE: Establish button listeners for field and location search

        // Button: Search
        binding.buttonSearch.setOnClickListener {

            // Hide the soft-keyboard
            hideKeyboard()

            // gets the list of representatives from the API
            viewModel.getRepresentativesList()

        }

        // Button: Location
        binding.buttonLocation.setOnClickListener {

            // Hide the soft-keyboard
            hideKeyboard()

            // gets the list of representatives based on user's geo-location
            getRepresentativeWithPermissionHandling()

        }

        return binding.root

    }

    @Suppress("DEPRECATION")
    private fun getRepresentativeWithPermissionHandling() {

        return if (isPermissionGranted()) {

            // gets the list of representatives based on user's geo-location
            getLocation()

        } else {

            // Request location permissions
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_ACCESS_FINE_LOCATION
            )

        }

    }

    @Suppress("DEPRECATION")
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

    //DONE: Check if permission is already granted and return (true = granted, false = denied/other)
    private fun isPermissionGranted(): Boolean {

        // Check if permission is already granted and return (true = granted, false = denied/other)
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

            // Success: Get the location
            .addOnSuccessListener { location ->
                location?.let {

                    // geoCodeLocation is a helper function to change the lat/long location
                    // to a human readable street address
                    val address = geoCodeLocation(location)

                    // get list of representatives around the user's geo location
                    viewModel.getRepresentativesList(address)
                }
            }

            // Failure: Log Error Message
            .addOnFailureListener { e ->
                Timber.e("Error: %s", e.localizedMessage)
            }
    }

    /**
     * geoCodeLocation is a helper function to change the lat/long location to a human readable street address
     */
    @Suppress("DEPRECATION")
    private fun geoCodeLocation(location: Location): Address? {

        // Geocoder is an Android class that helps you map between coordinates and addresses
        val geocoder = Geocoder(requireContext(), Locale.getDefault())

        // .getFromLocation() returns a list of addresses that are known to describe the area immediately
        // surrounding the given latitude and longitude
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)

            ?.map { address ->

                // Note: this might be null, that's why I am using a default fallback value of 00000
                val zipcode = if (address.postalCode == null) "00000" else address.postalCode

                Address(

                    // Returns the thoroughfare name of the address, for example, "1600 Ampitheater Parkway", which may be null
                    address.thoroughfare,

                    // Returns the sub-administrative area name of the address, for example, "Mountain View", which may be null
                    address.subThoroughfare,

                    // Returns the sub-administrative area name of the address, for example, "CA", which may be null
                    address.locality,

                    // Returns the sub-administrative area name of the address, for example, "Santa Clara", which may be null
                    address.adminArea,

                    // Returns the postal code of the address, for example, "94043", which may be null
                    zipcode
                )

            }
            ?.first()
    }

    // Hide the soft-keyboard
    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}
