package com.example.android.politicalpreparedness.network.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AdministrationBody(

    // Name
    val name: String? = null,

    // URL: Election Info
    val electionInfoUrl: String? = null,

    // URL: Voting Location Finder
    val votingLocationFinderUrl: String? = null,

    // URL: Ballot Info
    val ballotInfoUrl: String? = null,

    // Contact Address
    val correspondenceAddress: Address? = null

)
