package ru.wood.cuber.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.wood.cuber.data.MyCalculatesContentsTab
import ru.wood.cuber.data.MyCalculation
import ru.wood.cuber.data.MyСontainer

@Dao
interface DaoContains {
    @Insert
    fun save(myСontainer: MyСontainer) : Long

    @Insert
    fun saveContentTab (contentTab: MyCalculatesContentsTab) : Long

    @Delete
    fun delete(myСontainer: MyСontainer): Int

    @Query("DELETE FROM MyCalculatesContentsTab WHERE MyCalculatesContentsTab.idOfCalculates=:idOfCalculates")
    fun deleteContent(idOfCalculates: Long) : Int


    @Query("SELECT * FROM MyСontainer LEFT JOIN MyCalculatesContentsTab " +
            "ON MyСontainer.id==MyCalculatesContentsTab.idOfContainers " +
            "WHERE MyCalculatesContentsTab.idOfCalculates=:calculateId")
    fun load(calculateId: Long) : List<MyСontainer>

}