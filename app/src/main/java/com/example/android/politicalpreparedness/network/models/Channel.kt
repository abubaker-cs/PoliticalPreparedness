package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Channel(

    // Type
    val type: String,

    // ID
    val id: String

) : Parcelable
