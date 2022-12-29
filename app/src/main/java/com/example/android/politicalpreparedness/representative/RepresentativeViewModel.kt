package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.*
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.utils.CivicsApiStatus
import kotlinx.coroutines.launch
import timber.log.Timber

class RepresentativeViewModel(private val savedHandle: SavedStateHandle) : ViewModel() {

    private val apiService = CivicsApi.retrofitService

    //DONE: Establish live data for representatives and address

    // API Status
    private val _apiStatus: MutableLiveData<CivicsApiStatus> = MutableLiveData()
    val apiStatus: LiveData<CivicsApiStatus>
        get() = _apiStatus

    // Representatives
    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    // Address
    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    //DONE: Create function to fetch representatives from API from a provided address
    init {

        _address.value = Address("", null, "", "", "")

        val list: List<Representative>? = savedHandle["representatives"]

        if (list != null) {
            _representatives.value = list
        }

    }

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //DONE: Create function to get list of representatives around the user's geo location
    fun getRepresentativesList(address: Address?) {

        // Set the Civics API status to loading
        _apiStatus.value = CivicsApiStatus.LOADING

        // viewModelScope is a coroutine scope tied to the lifecycle of the ViewModel
        viewModelScope.launch {

            _representatives.value = arrayListOf()

            // If address is not null, then get the representatives
            if (address != null) {

                // Get the representatives from the API
                try {

                    // Updated the value of _address
                    _address.value = address

                    // apiServices.getRepresentatives is used to get the representatives from the API
                    val (offices, officials) = apiService.getRepresentatives(_address.value?.toFormattedString()!!)

                    // offices.flatMap is a function that takes a list of offices and officials and
                    // returns a list of representatives
                    _representatives.value =
                        offices.flatMap { office -> office.getRepresentatives(officials) }

                    // saveHandle is a SavedStateHandle that is passed to the ViewModel
                    savedHandle["representatives"] = _representatives.value

                    // Set the Civics API status to done
                    _apiStatus.value = CivicsApiStatus.DONE

                } catch (e: Exception) {

                    // Set the Civics API status to error
                    _apiStatus.value = CivicsApiStatus.ERROR

                    // Log error message using e.localizedMessage
                    // Note: e.localizedMessage creates a localized description of this throwable.
                    Timber.e(
                        "Error: %s", e.localizedMessage
                    )

                }
            }
        }

    }


    // gets the list of representatives from the API
    fun getRepresentativesList() {
        getRepresentativesList(_address.value)
    }

}
