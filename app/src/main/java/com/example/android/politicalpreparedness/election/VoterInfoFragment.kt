package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.utils.CivicsApiStatus

class VoterInfoFragment : Fragment() {

    private var _binding: FragmentVoterInfoBinding? = null
    private val binding get() = _binding!!

    //DONE: Add ViewModel values and create ViewModel
    private val viewModel by viewModels<VoterInfoViewModel> {
        VoterInfoViewModelFactory(
            ElectionDatabase.getInstance(requireContext()).electionDao,
            CivicsApi.retrofitService
        )
    }

    /**
     * Inflates the layout with Data Binding, sets its lifecycle owner to the OverviewFragment to
     * enable Data Binding to observe LiveData, and sets up the navigate the user based on the context.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */

        // Retrieve arguments
        val voterInfoFragmentArgs = VoterInfoFragmentArgs.fromBundle(requireArguments())

        // Get: Election ID
        val electionId = voterInfoFragmentArgs.argElectionId

        // Get: Division
        val division = voterInfoFragmentArgs.argDivision


        //DONE: Add binding values
        _binding = FragmentVoterInfoBinding.inflate(inflater, container, false)

        binding.apply {

            // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
            lifecycleOwner = viewLifecycleOwner

            // Set the ViewModel for databinding - this allows the bound layout access
            voterInfoViewModel = viewModel

        }

        // getVoterInfo
        viewModel.getVoterInfo(electionId, division)

        // Observer: API Status
        viewModel.apiStatus.observe(viewLifecycleOwner) {

            // Display ErrorDialog if API Status is ERROR
            it?.let {
                if (it == CivicsApiStatus.ERROR) showRequestErrorDialog()
            }

        }

        //DONE: Handle loading of URLs
        // Observer: URL (web links)
        viewModel.url.observe(viewLifecycleOwner) { it ->

            // Open URL in Browser
            it?.let {

                // Open URL in Browser
                loadUrl(it)

                // Done navigating
                viewModel.navigateToUrlCompleted()
            }

        }

        return binding.root

    }

    //DONE: Create method to load URL intents
    private fun showRequestErrorDialog() {

        // Create an AlertDialog
        AlertDialog.Builder(requireContext())

            // Title
            .setTitle("Error")

            // Message
            .setMessage("The voter information can't be retrieved. Click OK to go back.")

            // Button: Cancel (Hide it)
            .setCancelable(false)

            // Button: OK
            .setPositiveButton("Ok") { dialog, _ ->

                // Close the dialog
                dialog.dismiss()

                // Navigate back
                this.findNavController().popBackStack()

            }.show()
    }

    private fun loadUrl(url: String) {

        // Create an Intent to view the URL
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        // Start the Intent
        startActivity(browserIntent)

    }

    // This will avoid memory leaks
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
