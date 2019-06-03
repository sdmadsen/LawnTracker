package com.sdmadsen.steve.lawntracker

import androidx.room.TypeConverter
import java.sql.Time
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Calendar? = value?.let { time ->
        GregorianCalendar().also { calendar ->
            calendar.timeInMillis = time
        }
    }

    @TypeConverter
    fun dateToTimestamp(timestamp: Calendar?): Long? = timestamp?.timeInMillis

    @TypeConverter
    fun directionFromString(value: String?): Direction? {
        return value?.let { Direction.getDirectionByText(it) }
    }

    @TypeConverter
    fun directionToString(direction: Direction?): String? {
        return direction?.text
    }

    @TypeConverter
    fun statusFromString(value: String?): Status? {
        return value?.let { Status.getStatusByText(it) }
    }

    @TypeConverter
    fun statusToString(status: Status?): String? {
        return status?.text
    }
}