package ru.wood.cuber.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.wood.cuber.data.MyOrder

@Dao
interface DaoMyOrder {

    @Insert
    fun save(myCalculations: MyOrder) : Long

    @Query("SELECT * FROM MyOrder")
    fun load() : List<MyOrder>

    @Delete
    fun delete(myCalculations: MyOrder): Int
}