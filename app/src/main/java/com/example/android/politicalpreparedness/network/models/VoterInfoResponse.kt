package com.example.android.politicalpreparedness.network.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class VoterInfoResponse(

    // Election
    val election: Election,

    // Polling Locations
    val pollingLocations: String? = null,

    // Contests
    val contests: String? = null,

    // State
    val state: List<State>? = null,

    // Election Officials
    val electionElectionOfficials: List<ElectionOfficial>? = null

)
