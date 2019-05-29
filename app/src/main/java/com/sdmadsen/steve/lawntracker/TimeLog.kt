package com.sdmadsen.steve.lawntracker

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.sql.Time

@Entity(tableName = "time_log_table",
    foreignKeys = arrayOf(ForeignKey(entity = Mow::class,
        parentColumns = arrayOf("refId"),
        childColumns = arrayOf("mowId"),
        onDelete = ForeignKey.CASCADE)))
class TimeLog(
    @PrimaryKey
    @ColumnInfo (name = "refId") val refId: String,
    @ColumnInfo (name = "mowId") val mowId: String,
    @ColumnInfo (name = "status") val status: Status,
    @ColumnInfo (name = "date_created") val date_created: Time


)