package com.aatif.sevenminutesworkout.room.dao


import androidx.room.Dao
import androidx.room.Query
import com.aatif.sevenminutesworkout.room.entities.History

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history")
    fun getCompleteHistory(): List<History>

    @Query("SELECT * FROM history WHERE datestr >= :query_datestr")
    fun getHistoryAfter(query_datestr: String): List<History>

    @Query("INSERT INTO history values (:uuid , :date, :id, :name, :imageResource)")
    fun updateHistory(uuid:String , date: String , id:Int, name:String, imageResource:Int)
}
