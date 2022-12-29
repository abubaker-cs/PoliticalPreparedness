package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApiService

//DONE: Create Factory to generate ElectionViewModel with provided election datasource
class ElectionsViewModelFactory(
    private val localDataSource: ElectionDao,
    private val remoteDataSource: CivicsApiService
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        // If the model class is not the ElectionViewModel, throw an exception
        if (modelClass.isAssignableFrom(ElectionsViewModel::class.java)) {

            // return ElectionViewModel() as T with the localDataSource and remoteDataSource
            return ElectionsViewModel(localDataSource, remoteDataSource) as T

        }

        // Error: Unknown ViewModel class
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
