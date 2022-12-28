package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Division(

    // ID
    val id: String,

    // Country
    val country: String,

    // State
    val state: String

) : Parcelable
