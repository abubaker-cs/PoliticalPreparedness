package com.example.android.politicalpreparedness.election.elections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election

class ElectionsFragment : Fragment() {

    //DONE: Declare ViewModel
    private val viewModel by viewModels<ElectionsViewModel> {
        ElectionsViewModelFactory(
            ElectionDatabase.getInstance(requireContext()).electionDao,
            CivicsApi.retrofitService
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //DONE: Add binding values
        val binding = FragmentElectionBinding.inflate(inflater)

        binding.apply {

            lifecycleOwner = viewLifecycleOwner

            //DONE: Add ViewModel values and create ViewModel
            viewModel = viewModel

            //DONE: Link elections to voter info
            //DONE: Initiate recycler adapters
            //DONE: Populate recycler adapters

            // Upcoming Elections
            upcomingElections.adapter = ElectionListAdapter(ElectionListAdapter.ElectionListener {
                viewModel?.displayVoterInfo(it)
            })

            // Saved Elections
            savedElections.adapter = ElectionListAdapter(ElectionListAdapter.ElectionListener {
                viewModel?.displayVoterInfo(it)
            })
        }

        viewModel.navigateToVoterInfoFragment.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                navigateToDetailFragment(it)
                viewModel.displayVoterInfoComplete()
            }
        })

        return binding.root

    }

    //DONE: Refresh adapters when fragment loads
    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    private fun navigateToDetailFragment(election: Election) {
        this.findNavController().navigate(
            com.example.android.politicalpreparedness.election.ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                election.id,
                election.division
            )
        )
    }

}
