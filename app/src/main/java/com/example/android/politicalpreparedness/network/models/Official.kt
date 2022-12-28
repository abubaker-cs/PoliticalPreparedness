package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Official(

    // Name
    val name: String,

    // Address
    val address: List<Address>? = null,

    // Party
    val party: String? = null,

    // Phones
    val phones: List<String>? = null,

    // URLs
    val urls: List<String>? = null,

    // Photo URL (avatar)
    val photoUrl: String? = null,

    // Channels
    val channels: List<Channel>? = null

) : Parcelable
