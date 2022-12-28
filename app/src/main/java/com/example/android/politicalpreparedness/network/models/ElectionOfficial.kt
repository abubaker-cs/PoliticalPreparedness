package com.example.android.politicalpreparedness.network.models

import com.squareup.moshi.Json

data class ElectionOfficial(

    // Name
    val name: String,

    // Title
    val title: String,

    // Phone Number
    @Json(name = "officePhoneNumber")
    val phone: String,

    // Fax Number
    @Json(name = "faxNumber")
    val fax: String,

    // Email Address
    val emailAddress: String

)
