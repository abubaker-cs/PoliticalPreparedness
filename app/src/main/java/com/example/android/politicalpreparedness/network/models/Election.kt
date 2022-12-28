package com.example.android.politicalpreparedness.network.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.util.*

@Entity(tableName = "election_table")
data class Election(

    // Primary Key
    @PrimaryKey
    val id: Int,

    // Name
    @ColumnInfo(name = "name")
    val name: String,

    // Election Day
    @ColumnInfo(name = "electionDay")
    val electionDay: Date,

    // Division
    @Embedded(prefix = "division_") @Json(name = "ocdDivisionId")
    val division: Division

)
