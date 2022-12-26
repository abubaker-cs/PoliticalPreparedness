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

    //DONE: Create function get address from geo location
    fun getRepresentativesList(address: Address?) {

        _apiStatus.value = CivicsApiStatus.LOADING

        viewModelScope.launch {

            _representatives.value = arrayListOf()

            if (address != null) {

                try {

                    _address.value = address
                    val (offices, officials) = apiService.getRepresentatives(_address.value?.toFormattedString()!!)

                    _representatives.value =
                        offices.flatMap { office -> office.getRepresentatives(officials) }

                    savedHandle["representatives"] = _representatives.value

                    _apiStatus.value = CivicsApiStatus.DONE

                } catch (e: Exception) {
                    Timber.e(
                        "Error: %s", e.localizedMessage
                    )
                    _apiStatus.value = CivicsApiStatus.ERROR
                }
            }
        }

    }

    fun getRepresentativesList() {
        Timber.d("address: %s", _address.value)
        getRepresentativesList(_address.value)
    }

    //TODO: Create function to get address from individual fields

}
