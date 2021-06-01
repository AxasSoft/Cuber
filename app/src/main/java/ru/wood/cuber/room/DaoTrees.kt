package ru.wood.cuber.room

import androidx.room.Dao

@Dao
interface DaoTrees {

    /*@Query("SELECT * FROM TreePositon LEFT JOIN ContainerContents AS Container WHERE Container.idOfTreePosition=:idContainer")
    fun getTreePositions(idContainer: Int): List<TreePositon>*/
   /* @Insert
    fun addTreePosition(vararg treePosition: TreePositon): List<Long>


    @Query("SELECT * FROM Сontainer")
    fun getContainers(): List<Сontainer>
    @Insert
    fun addContainer(vararg container: Сontainer): List<Long>*/

}