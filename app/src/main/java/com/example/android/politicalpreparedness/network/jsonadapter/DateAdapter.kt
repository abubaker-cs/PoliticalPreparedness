package com.example.android.politicalpreparedness.network.jsonadapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.*

/**
 * DateAdapter is a custom adapter that is used to convert a date string to a Date object as
 * Moshi.Builder().add(DateAdapter()) in CivicsApiService.kt
 */
class DateAdapter {

    // SimpleDateFormat is a concrete class for formatting and parsing dates in a locale-sensitive manner.
    private val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @FromJson
    fun dateFromJson(dateStr: String): Date? {
        return format.parse(dateStr)
    }

    @ToJson
    fun dateToJson(date: Date): String {
        return format.format(date)
    }
}
