package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election

class ElectionsFragment : Fragment() {

    private var _binding: FragmentElectionBinding? = null
    private val binding get() = _binding!!

    //DONE: Declare ViewModel
    private val viewModel by viewModels<ElectionsViewModel> {
        ElectionsViewModelFactory(
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

        //DONE: Add binding values
        _binding = FragmentElectionBinding.inflate(inflater, container, false)


        binding.apply {

            // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
            lifecycleOwner = viewLifecycleOwner

            // Set the ViewModel for databinding - this allows the bound layout access
            electionsViewModel = viewModel

            // Upcoming Elections
            recyclerUpcoming.adapter = ElectionListAdapter(ElectionListAdapter.ElectionListener {
                viewModel.displayVoterInfo(it)
            })

            // Saved Elections
            recyclerSaved.adapter = ElectionListAdapter(ElectionListAdapter.ElectionListener {
                viewModel.displayVoterInfo(it)
            })

        }

        // Observer: Navigate to VoterInfoFragment
        viewModel.navigateToVoterInfo.observe(viewLifecycleOwner) {
            if (null != it) {
                navigateToDetailFragment(it)
                viewModel.displayVoterInfoComplete()
            }
        }

        return binding.root

    }

    //DONE: Refresh adapters when fragment loads
    override fun onResume() {

        super.onResume()

        // Refresh the list of Saved Elections from the local database
        viewModel.refresh()

    }

    // Navigate to VoterInfoFragment
    private fun navigateToDetailFragment(election: Election) {

        // Goto: VoterInfoFragment with election ID and division
        this.findNavController().navigate(
            ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                election.id,
                election.division
            )
        )

    }

    // This will avoid memory leaks
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
