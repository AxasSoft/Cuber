package ru.wood.cuber.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.wood.cuber.data.MyCalculation

@Dao
interface DaoMyCalculates {

    @Insert
    fun save(myCalculations: MyCalculation) : Long

    @Query("SELECT * FROM MyCalculation")
    fun load() : List<MyCalculation>

    @Delete
    fun delete(myCalculations: MyCalculation): Int
}