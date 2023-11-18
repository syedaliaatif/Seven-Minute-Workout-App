package com.aatif.sevenminutesworkout.room.dao


import androidx.room.Dao
import androidx.room.Query
import com.aatif.sevenminutesworkout.room.entities.History

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history")
    fun getCompleteHistory(): List<History>

    @Query("SELECT * FROM history WHERE datestr >= :datestr")
    fun getHistoryAfter(datestr: String): List<History>

    @Query("INSERT INTO history values (:uuid , :date, :id, :name, :imageResource,:sessionUUID)")
    fun updateHistory(uuid:String , date: String , id:Int, name:String, imageResource:Int, sessionUUID: String)
}
