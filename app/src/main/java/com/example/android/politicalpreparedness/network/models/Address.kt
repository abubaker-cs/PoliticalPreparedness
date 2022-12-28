package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(

    // Address lines 1 and 2
    var line1: String,
    var line2: String? = null,

    // City
    var city: String,

    // State
    var state: String,

    // Zip (Postal Code)
    var zip: String

) : Parcelable {

    // toFormattedString() extension function to return a properly formatted address
    fun toFormattedString(): String {

        // add line 1 to the output
        var output = line1.plus("\n")

        // if line2 is not null, add it to the output
        if (!line2.isNullOrEmpty()) output = output.plus(line2).plus("\n")

        // add city, state, and zip to the output
        output = output.plus("$city, $state $zip")

        // return the output
        return output
    }
}
