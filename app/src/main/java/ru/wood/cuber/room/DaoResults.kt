package ru.wood.cuber.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.data.VolumesTab

@Dao
interface DaoResults {

    @Query("SELECT *, count(TreePosition.id) AS quantity " +
            "FROM TreePosition " +
            "LEFT JOIN ContainerContentsTab " +
            "ON TreePosition.id==ContainerContentsTab.idOfTreePosition " +
            "WHERE ContainerContentsTab.idOfContainer=:container " +
            "GROUP BY TreePosition.length")
    fun loadResult(container: Long) : List<TreePosition>

    @Insert
    fun saveOneVolume(position : VolumesTab): Long

    @Query("SELECT * FROM VolumesTab WHERE VolumesTab.length=:length AND VolumesTab.idOfContainer=:container")
    fun groupByLength(length: Double, container: Long): List<VolumesTab>

    @Query("DELETE FROM VolumesTab WHERE idOfContainer=:container")
    fun delete(container: Long): Int




}