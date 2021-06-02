package ru.wood.cuber.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.wood.cuber.data.ContainerContentsTab
import ru.wood.cuber.data.MyСontainer
import ru.wood.cuber.data.TreePosition

@Dao
interface DaoTrees {
    @Insert
    fun save(tree: TreePosition) : Long

    @Insert
    fun saveContentTab (containerContentsTab: ContainerContentsTab) : Long

    @Delete
    fun delete(tree: TreePosition): Int

    @Query("DELETE FROM ContainerContentsTab WHERE ContainerContentsTab.idOfContainer=:idOfContainer")
    fun deleteContent(idOfContainer: Long) : Int

    @Query("SELECT * FROM TreePosition LEFT JOIN ContainerContentsTab ON TreePosition.id==ContainerContentsTab.idOfTreePosition WHERE ContainerContentsTab.idOfContainer=:container")
    fun load(container: Long) : List<TreePosition>

}