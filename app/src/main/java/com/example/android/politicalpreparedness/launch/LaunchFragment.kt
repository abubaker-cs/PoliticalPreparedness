package com.example.android.politicalpreparedness.launch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentLaunchBinding

class LaunchFragment : Fragment() {

    private var _binding: FragmentLaunchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate view and obtain an instance of the binding class.
        _binding = FragmentLaunchBinding.inflate(inflater, container, false)

        // viewLifeCycleOwner is used to observe LiveData
        binding.lifecycleOwner = viewLifecycleOwner

        // Button: Representatives
        binding.representativeButton.setOnClickListener {

            // Open: Representatives Fragment
            this.findNavController()
                .navigate(LaunchFragmentDirections.actionLaunchFragmentToRepresentativeFragment())

        }

        // Button: Upcoming Elections
        binding.upcomingButton.setOnClickListener {

            // Open: Elections Fragment
            this.findNavController()
                .navigate(LaunchFragmentDirections.actionLaunchFragmentToElectionsFragment())

        }

        return binding.root
    }

    // This will avoid memory leaks
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
