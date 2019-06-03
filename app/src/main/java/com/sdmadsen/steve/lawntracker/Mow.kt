package com.sdmadsen.steve.lawntracker

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "mow_table")
class Mow(
    @PrimaryKey
    @ColumnInfo (name = "refId") val refId: String,
    @ColumnInfo (name = "direction") val direction: Direction,
    @ColumnInfo (name = "trimmed") val trimmed: Boolean? = null,
    @ColumnInfo (name = "status") val status: Status,
    @ColumnInfo (name = "date_created") val date_created: Calendar


)