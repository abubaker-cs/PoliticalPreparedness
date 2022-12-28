package com.example.android.politicalpreparedness.network.jsonadapter

import com.example.android.politicalpreparedness.network.models.Division
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

/**
 * ElectionAdapter is a custom adapter that is used to convert a division object to a String as
 * Moshi.Builder().add(ElectionAdapter()) in CivicsApiService.kt
 */
class ElectionAdapter {

    // divisionFromJson() is used to convert a division object to a String
    @FromJson
    fun divisionFromJson(ocdDivisionId: String): Division {

        // countryDelimiter
        val countryDelimiter = "country:"

        // stateDelimiter
        val stateDelimiter = "state:"

        // country
        val country = ocdDivisionId.substringAfter(countryDelimiter, "")
            .substringBefore("/")

        // state
        val state = ocdDivisionId.substringAfter(stateDelimiter, "")
            .substringBefore("/")

        // Return a Division object
        return Division(
            ocdDivisionId,
            country,
            state
        )
    }

    // divisionToJson() is used to convert a String to a division object
    @ToJson
    fun divisionToJson(division: Division): String {
        return division.id
    }
}
