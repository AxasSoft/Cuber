package ru.wood.cuber.interactors

import ru.wood.cuber.Loger
import ru.wood.cuber.data.ContainerContentsTab
import ru.wood.cuber.data.MyCalculatesContentsTab
import ru.wood.cuber.data.MyСontainer
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.repositories.RepositoryContains
import ru.wood.cuber.repositories.RepositoryTrees
import javax.inject.Inject

class LoadTrees @Inject constructor(val repository: RepositoryTrees): UseCase<List<TreePosition>, Long?>(){

    override suspend fun run(params: Long?): List<TreePosition> {
        return repository.loadList(params!!)
    }
}

class SaveOneTree @Inject constructor(val repository: RepositoryTrees): UseCase<Long, TreePosition>(){

    override suspend fun run(params: TreePosition) :Long{
        val idOfConteiner=repository.saveOne(params)
        Loger.log("id of saved position $idOfConteiner")
        return idOfConteiner
    }
}

class SaveTreeContent @Inject constructor(val repository: RepositoryTrees): UseCase<Boolean, ContainerContentsTab>(){

    override suspend fun run(params: ContainerContentsTab) :Boolean{
        val ok=repository.saveContent(params)
        Loger.log("id of saved position $ok")
        return ok!=0L
    }
}
class DeleteOneTree @Inject constructor(val repository: RepositoryTrees): UseCase<Boolean, TreePosition>() {

    override suspend fun run(params: TreePosition): Boolean {
        val ok = repository.deleteOne(params)
        Loger.log("id of saved position $ok")
        return ok > 0
    }
}

class DeleteTreesContent @Inject constructor(val repository: RepositoryTrees): UseCase<Boolean, Long>() {

    override suspend fun run(params: Long): Boolean {
        val ok = repository.deleteContent(params)
        Loger.log("id of saved position $ok")
        return ok > 0
    }
}