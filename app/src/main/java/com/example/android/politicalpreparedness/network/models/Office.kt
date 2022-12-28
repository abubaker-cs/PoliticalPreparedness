package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import com.example.android.politicalpreparedness.representative.model.Representative
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Office(

    // Name
    val name: String,

    // Division ID
    @Json(name = "divisionId") val division: Division,

    // List of indices for officials
    @Json(name = "officialIndices") val officials: List<Int>

) : Parcelable {

    // getRepresentatives() returns a list of Representative objects from a list of:
    // 1. Officials
    // 2. Office
    fun getRepresentatives(officials: List<Official>): List<Representative> {

        return this.officials.map { index ->

            // Get the official at the index
            Representative(officials[index], this)

        }

    }
}
