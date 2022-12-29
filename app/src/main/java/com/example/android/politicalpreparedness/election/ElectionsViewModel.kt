package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.utils.CivicsApiStatus
import kotlinx.coroutines.launch
import timber.log.Timber

//DONE: Construct ViewModel and provide election datasource

class ElectionsViewModel(
    private val database: ElectionDao,
    private val apiService: CivicsApiService
) : ViewModel() {

    // API Status
    private val _apiStatus = MutableLiveData<CivicsApiStatus>()
    val apiStatus: LiveData<CivicsApiStatus>
        get() = _apiStatus

    // IsDbLoading
    private val _isDbLoading = MutableLiveData<Boolean>()
    val isDbLoading: LiveData<Boolean>
        get() = _isDbLoading

    // Upcoming Elections
    private val _upComingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upComingElections

    // Saved Elections
    private val _savedElections = MutableLiveData<List<Election>>()
    val savedElections: LiveData<List<Election>>
        get() = _savedElections

    // Navigate to Voter Info
    private val _navigateToVoterInfo = MutableLiveData<Election>()
    val navigateToVoterInfo: LiveData<Election>
        get() = _navigateToVoterInfo

    /**
     * Initial setup
     */
    init {

        // Get: Upcoming Elections
        getUpcomingElectionsFromAPI()

        // Get: Saved Elections
        getSavedElectionsFromDatabase()

    }

    /**
     * Get upcoming elections from API
     */
    private fun getUpcomingElectionsFromAPI() {

        // API Status: Loading
        _apiStatus.value = CivicsApiStatus.LOADING

        // Coroutine to get the elections from the API
        viewModelScope.launch {

            try {

                //  Get: Upcoming Elections
                val response = apiService.getElections()

                // API Status: Done
                _apiStatus.value = CivicsApiStatus.DONE

                // Set: Upcoming Elections to the response.elections
                _upComingElections.value = response.elections

            } catch (e: Exception) {

                // API Status: Error
                _apiStatus.value = CivicsApiStatus.ERROR

                // Log: Error
                Timber.e("Error: %s", e.localizedMessage)

                // Set: Upcoming Elections to empty list
                _upComingElections.value = ArrayList()

            }
        }
    }

    /**
     * Get saved elections from database
     */
    private fun getSavedElectionsFromDatabase() {

        // IsDbLoading: True
        _isDbLoading.value = true

        // Coroutine to get the elections from the database
        viewModelScope.launch {

            // Get: Saved Elections
            _savedElections.value = database.getElections()

            // IsDbLoading: False
            _isDbLoading.value = false

        }
    }

    // Refresh: Saved Elections from the local database
    fun refresh() {
        getSavedElectionsFromDatabase()
    }

    // Display: Voter Info
    fun displayVoterInfo(election: Election) {
        _navigateToVoterInfo.value = election
    }

    // Display: Voter Info Completed
    fun displayVoterInfoComplete() {
        _navigateToVoterInfo.value = null
    }

}
