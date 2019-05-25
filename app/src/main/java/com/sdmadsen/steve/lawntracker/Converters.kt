package com.sdmadsen.steve.lawntracker

import androidx.room.TypeConverter
import java.sql.Time

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Time? {
        return value?.let { Time(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Time?): Long? {
        return date?.time?.toLong()
    }

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