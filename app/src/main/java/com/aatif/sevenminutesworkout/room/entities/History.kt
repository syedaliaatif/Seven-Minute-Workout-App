package com.aatif.sevenminutesworkout.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class History (
    @PrimaryKey val uuid: String,
    @ColumnInfo(name = "datestr") val datestr: String,
    @ColumnInfo(name = "exercise_id") val id: Int,
    @ColumnInfo(name = "exercise_name") val name:String,
    @ColumnInfo(name = "exercise_image_resource")val imageResource:Int,
    @ColumnInfo(name = "session_uuid") val sessionUUID: String
)