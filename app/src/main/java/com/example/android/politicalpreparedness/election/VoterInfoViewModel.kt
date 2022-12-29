package com.example.android.politicalpreparedness.election

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.AdministrationBody
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.utils.CivicsApiStatus
import kotlinx.coroutines.launch
import timber.log.Timber

class VoterInfoViewModel(
    private val dataSource: ElectionDao,
    private val apiService: CivicsApiService
) : ViewModel() {

    //DONE: Add live data to hold voter info

    // API Status
    private val _apiStatus = MutableLiveData<CivicsApiStatus>()
    val apiStatus: LiveData<CivicsApiStatus>
        get() = _apiStatus

    //DONE: Add var and methods to populate voter info
    //DONE: Add var to save and remove elections to local database

    // Election
    private val _election = MutableLiveData<Election>()
    val election: LiveData<Election>
        get() = _election

    // Administration Body
    private val _administrationBody = MutableLiveData<AdministrationBody>()
    val administrationBody: LiveData<AdministrationBody>
        get() = _administrationBody

    //DONE: Add var and methods to support loading URLs

    // URL
    private val _url = MutableLiveData<String>()
    val url: LiveData<String>
        get() = _url

    // isElectionSaved (Boolean)
    private val _isElectionSaved = MutableLiveData<Boolean>()
    val isElectionSaved: LiveData<Boolean>
        get() = _isElectionSaved

    /**
     * getVoterInfo() - Get Voter Info from API
     */
    fun getVoterInfo(electionId: Int, division: Division) {

        _apiStatus.value = CivicsApiStatus.LOADING

        viewModelScope.launch {
            try {

                val savedElection: Election? = dataSource.getElection(electionId)

                //DONE: Populate initial state of save button to reflect proper action based on election saved status
                _isElectionSaved.value = savedElection != null

                val address = "${division.state}, ${division.country}"
                val voterInfoResponse = apiService.getVoterInfo(address, electionId)

                _election.value = voterInfoResponse.election

                _administrationBody.value =
                    voterInfoResponse.state?.first()?.electionAdministrationBody

                _apiStatus.value = CivicsApiStatus.DONE
            } catch (e: Exception) {

                // API Status: Error
                _apiStatus.value = CivicsApiStatus.ERROR

                // Log Error message
                Timber.e("Error when retrieving voter information")

            }
        }
    }

    /**
     * navigateToUrl() - Navigate to URL
     */
    fun navigateToUrl(url: String) {

        // Update live data to navigate to selected URL
        _url.value = url

    }

    /**
     * doneNavigating() - Done navigating
     */
    @SuppressLint("NullSafeMutableLiveData")
    fun navigateToUrlCompleted() {

        // DONE: Set _url to null
        _url.value = null

    }

    //DONE: Add var and methods to save and remove elections to local database
    /**
     * toggleElectionSaved() - Toggle election saved
     */
    fun toggleFollowElection() {

        // DONE: Toggle saved election
        viewModelScope.launch {
            _election.value?.let {

                if (_isElectionSaved.value == true) {

                    // Remove election from saved elections
                    dataSource.deleteElection(it)

                    // Set isElectionSaved to false
                    _isElectionSaved.value = false

                } else {

                    // Add election to saved elections
                    dataSource.insertElection(it)

                    // Set isElectionSaved to true
                    _isElectionSaved.value = true

                }
            }
        }
    }

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}
