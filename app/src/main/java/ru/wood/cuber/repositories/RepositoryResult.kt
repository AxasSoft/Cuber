package ru.wood.cuber.repositories

import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.room.DaoResults
import javax.inject.Inject

class RepositoryResult @Inject constructor(val dao: DaoResults) {

    fun loadResult(container: Long) : List<TreePosition>{
        return dao.loadResult(container)
    }
}