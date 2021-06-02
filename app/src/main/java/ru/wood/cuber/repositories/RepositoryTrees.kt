package ru.wood.cuber.repositories

import ru.wood.cuber.Loger
import ru.wood.cuber.data.ContainerContentsTab
import ru.wood.cuber.data.MyCalculatesContentsTab
import ru.wood.cuber.data.MyСontainer
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.room.DaoTrees
import javax.inject.Inject

class RepositoryTrees @Inject constructor(val dao: DaoTrees) {

    fun getListTreesForExample() : List<TreePosition>{
        val tree1= TreePosition(1, 5.0, 18.0, 3, 0.651)
        val tree2= TreePosition(2, 5.0, 18.0, 7, 0.951)
        val tree3= TreePosition(3, 5.0, 20.0, 5, 0.751)
        val tree4= TreePosition(4, 8.0, 18.0, 3, 0.651)
        val tree5= TreePosition(5, 5.0, 21.0, 5, 0.251)
        val tree6= TreePosition(6, 5.0, 24.0, 3, 0.751)
        val tree7= TreePosition(7, 5.0, 30.0, 2, 0.651)

        val list: List<TreePosition> = arrayListOf(tree1,tree2,tree3,tree4,tree5,tree6,tree7);
        return list
    }


    fun saveOne(one: TreePosition): Long{
        return dao.save(one)
    }
    fun saveContent(contentTab: ContainerContentsTab): Long{
        return dao.saveContentTab(contentTab)
    }

    fun loadList(containId: Long): List<TreePosition> {
        return dao.load(containId)
    }

    fun  deleteOne(one: TreePosition): Int{
        val resultDelete=dao.delete(one)
        Loger.log("resultDelete = $resultDelete")
        return resultDelete
    }
    fun deleteContent(idOfContainer: Long): Int{
        val resultContentDelete = dao.deleteContent(idOfContainer)
        Loger.log("resultContentDelete = $resultContentDelete")
        return resultContentDelete
    }
}