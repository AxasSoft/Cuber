package ru.wood.cuber.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.wood.cuber.data.ContainerContentsTab
import ru.wood.cuber.data.TreePosition

@Dao
interface DaoTrees {
    @Insert
    fun save(tree: TreePosition) : Long

    @Insert
    fun saveList(trees: List<TreePosition>) : List<Long>

    @Insert
    fun saveContentTab(containerContentsTab: ContainerContentsTab) : Long

    @Insert
    fun saveContentTabList(containerContentsTab: List<ContainerContentsTab>) : List<Long>

    @Delete
    fun delete(tree: TreePosition): Int

    @Query("DELETE FROM TreePosition " +
            "WHERE (TreePosition.length=:length AND TreePosition.diameter=:diameter)")
    fun delete(length: Double?, diameter: Int?): Int

    @Query("DELETE FROM ContainerContentsTab " +
            "WHERE ContainerContentsTab.idOfContainer=:idOfContainer")
    fun deleteContent(idOfContainer: Long) : Int

    @Query("SELECT *, count(TreePosition.id) AS quantity " +
            "FROM TreePosition " +
            "LEFT JOIN ContainerContentsTab " +
            "ON TreePosition.id==ContainerContentsTab.idOfTreePosition " +
            "WHERE ContainerContentsTab.idOfContainer=:container " +
            "GROUP BY TreePosition.diameter, TreePosition.length")
    fun load(container: Long) : List<TreePosition>

    @Query("UPDATE TreePosition SET length = :newLength  " +
            "WHERE id IN (SELECT idOfTreePosition FROM ContainerContentsTab " +
            "WHERE idOfContainer=:currentContainer) ")
    fun updateLength(currentContainer: Long, newLength: Double): Int


    @Query("SELECT * FROM TreePosition WHERE id=:id")
    fun onePositionById(id: Long): TreePosition


    @Query("DELETE FROM TreePosition WHERE id " +
            "IN (SELECT id FROM TreePosition " +
            "WHERE (TreePosition.diameter=:diameter " +
            "AND TreePosition.length=:length) LIMIT :limit)")
    fun deleteByLimit(diameter: Int, length: Double, limit: Int): Int

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @Query("UPDATE TreePosition SET length = :newLength, diameter=:newDiameter  " +
            "WHERE id IN (:idList) ")
    fun updatePositions(newDiameter: Int,newLength: Double, idList :List<Long>): Int

    @Query("SELECT id FROM TreePosition WHERE id IN(SELECT idOfTreePosition " +
            "FROM ContainerContentsTab " +
            "WHERE idOfContainer=:currentContainer) " +
            "AND diameter=:diameter AND length=:length")
    fun getPositions (currentContainer: Long, diameter: Int, length: Double): List<Long>

}