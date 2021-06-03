package ru.wood.cuber.interactors

import ru.wood.cuber.Loger
import ru.wood.cuber.data.*
import ru.wood.cuber.interactors.ParamsClasses.Limit
import ru.wood.cuber.interactors.ParamsClasses.NewParams
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

class SaveListTree @Inject constructor(val repository: RepositoryTrees): UseCase<List<Long>, List<TreePosition>>(){

    override suspend fun run(params: List<TreePosition>) :List<Long>{
        val ok=repository.saveList(params)
        return ok
    }
}

class SaveTreeContent @Inject constructor(val repository: RepositoryTrees): UseCase<Boolean, ContainerContentsTab>(){

    override suspend fun run(params: ContainerContentsTab) :Boolean{
        val ok=repository.saveContent(params)
        Loger.log("id of saved position $ok")
        return ok!=0L
    }
}

class SaveTreeContentList @Inject constructor(val repository: RepositoryTrees): UseCase<Boolean, List<ContainerContentsTab>>(){

    override suspend fun run(params: List<ContainerContentsTab>) :Boolean{
        val longs=repository.saveContentList(params)
        Loger.log("id of saved position $longs")
        return longs.isNotEmpty()
    }
}

class DeleteOneTree @Inject constructor(val repository: RepositoryTrees): UseCase<Boolean, TreePosition>() {

    override suspend fun run(params: TreePosition): Boolean {
        val length=params.length
        val diameter=params.diameter
        val ok = repository.delete(length,diameter)
        Loger.log("id of saved position $ok")
        return ok > 0
    }
}

class ClearOneContain @Inject constructor(val repository: RepositoryTrees): UseCase<Boolean, Long>() {

    override suspend fun run(params: Long): Boolean {
        val ok = repository.deleteContent(params)
        Loger.log("id of saved position $ok")
        return ok > 0
    }
}

class UpdateTreeLength @Inject constructor(val repository: RepositoryTrees): UseCase<Boolean, NewParams>() {
    override suspend fun run(params: NewParams): Boolean{
        val container=params.containerOfTrees
        val length=params.length
        val ok = repository.updateLength(container,length)
        return ok>0
    }
}

class UpdateTreePositions @Inject constructor(val repository: RepositoryTrees): UseCase<Boolean,  NewParams>() {
    override suspend fun run(params: NewParams): Boolean{
        val container=params.containerOfTrees
        val diameter=params.diameter
        val length =params.length
        val list =params.idList

        val ok = repository.updatePositions(container,diameter!!,length,list!!)
        return ok>0
    }
}

class OnePositionById @Inject constructor(val repository: RepositoryTrees): UseCase<TreePosition, Long?>() {
    override suspend fun run(params: Long?): TreePosition {
        return repository.getOne(params!!)
    }
}

class DeleteByLimit @Inject constructor(val repository: RepositoryTrees): UseCase<Boolean, Limit>() {
    override suspend fun run(params: Limit): Boolean {
        val diameter=params.diameter
        val length =params.length
        val limit =params.limit
        Loger.log(limit)
        val ok=repository.deleteByLimit(diameter, length, limit)
        return ok>0
    }
}
class getPositionsList @Inject constructor(val repository: RepositoryTrees) : UseCase<List<Long>, NewParams>() {
    override suspend fun run(params: NewParams): List<Long> {
        val diameter=params.diameter
        val length =params.length
        val container =params.containerOfTrees
        return repository.idList(container,diameter!!,length)
    }
}