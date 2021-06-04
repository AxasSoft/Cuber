package ru.wood.cuber.room

import androidx.room.Dao
import androidx.room.Query
import ru.wood.cuber.data.TreePosition

@Dao
interface DaoResults {

    @Query("SELECT *, count(TreePosition.id) AS quantity " +
            "FROM TreePosition " +
            "LEFT JOIN ContainerContentsTab " +
            "ON TreePosition.id==ContainerContentsTab.idOfTreePosition " +
            "WHERE ContainerContentsTab.idOfContainer=:container " +
            "GROUP BY TreePosition.length")
    fun loadResult(container: Long) : List<TreePosition>
}