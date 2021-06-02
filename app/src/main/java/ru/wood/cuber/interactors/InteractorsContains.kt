package ru.wood.cuber.interactors

import ru.wood.cuber.Loger
import ru.wood.cuber.data.MyCalculatesContentsTab
import ru.wood.cuber.data.MyСontainer
import ru.wood.cuber.repositories.RepositoryContains
import javax.inject.Inject

class LoadContains @Inject constructor(val repository: RepositoryContains): UseCase<List<MyСontainer>, Long?>(){

    override suspend fun run(params: Long?): List<MyСontainer> {
        return repository.loadList(params!!)
    }
}

class SaveOneContain @Inject constructor(val repository: RepositoryContains): UseCase<Long, MyСontainer>(){

    override suspend fun run(params: MyСontainer) :Long{
        val idOfConteiner=repository.saveOne(params)
        Loger.log("id of saved position $idOfConteiner")
        return idOfConteiner
    }
}

class SaveContent @Inject constructor(val repository: RepositoryContains): UseCase<Boolean, MyCalculatesContentsTab>(){

    override suspend fun run(params: MyCalculatesContentsTab) :Boolean{
        val ok=repository.saveContent(params)
        Loger.log("id of saved position $ok")
        return ok!=0L
    }
}
class DeleteOneContain @Inject constructor(val repository: RepositoryContains): UseCase<Boolean, MyСontainer>() {

    override suspend fun run(params: MyСontainer): Boolean {
        val ok = repository.deleteOne(params)
        Loger.log("id of saved position $ok")
        return ok > 0
    }
}

class DeleteContainerContent @Inject constructor(val repository: RepositoryContains): UseCase<Boolean, Long>() {

    override suspend fun run(params: Long): Boolean {
        val ok = repository.deleteContent(params)
        Loger.log("id of saved position $ok")
        return ok > 0
    }
}