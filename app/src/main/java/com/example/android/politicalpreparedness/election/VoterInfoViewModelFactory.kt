package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApiService

//DONE: Create Factory to generate VoterInfoViewModel with provided election datasource
class VoterInfoViewModelFactory(
    private val localDataSource: ElectionDao,
    private val remoteDataSource: CivicsApiService
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        // If the model class is not the VoterInfoViewModel, throw an exception
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {

            // return VoterInfoViewModel() as T with the localDataSource and remoteDataSource
            return VoterInfoViewModel(localDataSource, remoteDataSource) as T
        }

        // Error: Unknown ViewModel class
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}
