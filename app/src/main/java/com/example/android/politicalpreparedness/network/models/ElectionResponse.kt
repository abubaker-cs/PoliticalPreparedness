package com.example.android.politicalpreparedness.network.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ElectionResponse(

    // Kind of response (elections or voterinfo)
    val kind: String,

    // Elections list
    val elections: List<Election>
)
