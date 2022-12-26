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

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(
    private val database: ElectionDao,
    private val apiService: CivicsApiService
) : ViewModel() {

    private val _apiStatus = MutableLiveData<CivicsApiStatus>()
    val apiStatus: LiveData<CivicsApiStatus>
        get() = _apiStatus

    private val _isDbLoading = MutableLiveData<Boolean>()
    val isDbLoading: LiveData<Boolean>
        get() = _isDbLoading

    //DONE: Create live data val for upcoming elections
    private val _upComingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upComingElections

    //DONE: Create live data val for saved elections
    private var _savedElections = MutableLiveData<List<Election>>()
    val savedElections: LiveData<List<Election>>
        get() = _savedElections

    private val _navigateToVoterInfo = MutableLiveData<Election>()
    val navigateToVoterInfo: LiveData<Election>
        get() = _navigateToVoterInfo

    init {
        getUpcomingElectionsFromAPI()
        getSavedElectionsFromDatabase()
    }

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info
    private fun getUpcomingElectionsFromAPI() {
        _apiStatus.value = CivicsApiStatus.LOADING
        viewModelScope.launch {
            try {
                val response = apiService.getElections()
                _apiStatus.value = CivicsApiStatus.DONE
                _upComingElections.value = response.elections

            } catch (e: Exception) {
                Timber.e("Error: %s", e.localizedMessage)
                _apiStatus.value = CivicsApiStatus.ERROR
                _upComingElections.value = ArrayList()
            }
        }
    }

    private fun getSavedElectionsFromDatabase() {
        _isDbLoading.value = true
        viewModelScope.launch {
            _savedElections.value = database.getElections()
            _isDbLoading.value = false
        }
    }

    fun displayVoterInfo(election: Election) {
        _navigateToVoterInfo.value = election
    }

    fun displayVoterInfoComplete() {
        _navigateToVoterInfo.value = null
    }

    fun refresh() {
        getSavedElectionsFromDatabase()
    }

}
