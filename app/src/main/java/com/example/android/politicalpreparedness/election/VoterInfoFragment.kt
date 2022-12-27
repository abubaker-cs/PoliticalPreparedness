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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val voterInfoFragmentArgs =
            VoterInfoFragmentArgs.fromBundle(
                requireArguments()
            )
        val electionId = voterInfoFragmentArgs.argElectionId
        val division = voterInfoFragmentArgs.argDivision


        //DONE: Add binding values
        _binding = FragmentVoterInfoBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            voterInfoViewModel = viewModel
        }

        viewModel.getVoterInfo(electionId, division)

        viewModel.apiStatus.observe(viewLifecycleOwner) {
            it?.let {
                if (it == CivicsApiStatus.ERROR) showRequestErrorDialog()
            }
        }

        viewModel.url.observe(viewLifecycleOwner) { it ->
            it?.let {
                loadUrl(it)
                viewModel.navigateToUrlCompleted()
            }
        }
        return binding.root

        //DONE: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */


        //DONE: Handle loading of URLs

        //DONE: Handle save button UI state
        //DONE: Handle save button clicks

    }

    //DONE: Create method to load URL intents
    private fun showRequestErrorDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage("The voter information can't be retrieved. Click OK to go back.")
            .setCancelable(false)
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
                this.findNavController().popBackStack()
            }.show()
    }

    private fun loadUrl(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
        // viewModel.navigateToUrlCompleted()
    }

    // This will avoid memory leaks
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
