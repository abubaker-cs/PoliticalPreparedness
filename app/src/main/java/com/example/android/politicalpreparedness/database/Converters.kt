package com.example.android.politicalpreparedness.database

import androidx.room.TypeConverter
import java.util.*

/**
 * Converters() to allow Room to reference complex data types:
 * -----------------------------------------------------------
 * These methods allow Room to reference this converter when dealing with the Date class.
 */
class Converters {

    // 1. fromTimestamp()
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {

        // value?.let { Date(it) } returns a Date object if value is not null
        return value?.let { Date(it) }
    }

    // 2. dateToTimestamp()
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {

        // date?.time returns the number of milliseconds since January 1, 1970, 00:00:00 GMT
        return date?.time

    }
}
