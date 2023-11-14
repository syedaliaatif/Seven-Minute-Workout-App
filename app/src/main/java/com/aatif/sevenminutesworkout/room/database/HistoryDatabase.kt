package com.aatif.sevenminutesworkout.room.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.aatif.sevenminutesworkout.room.dao.HistoryDao
import com.aatif.sevenminutesworkout.room.entities.History

@Database(entities = [History::class], version = 1)
abstract class HistoryDatabase: RoomDatabase() {
    abstract fun historyDao():HistoryDao
}