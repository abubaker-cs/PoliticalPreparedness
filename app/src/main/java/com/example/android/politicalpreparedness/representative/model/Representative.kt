package com.example.android.politicalpreparedness.representative.model

import com.example.android.politicalpreparedness.network.models.Office
import com.example.android.politicalpreparedness.network.models.Official

data class Representative(

    // Official
    val official: Official,

    // Office
    val office: Office

)
