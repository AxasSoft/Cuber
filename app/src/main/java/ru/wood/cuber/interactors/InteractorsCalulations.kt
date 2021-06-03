package ru.wood.cuber.interactors

import ru.wood.cuber.Loger
import ru.wood.cuber.data.MyCalculation
import ru.wood.cuber.repositories.RepositoryMyCalculates
import javax.inject.Inject


class LoadForExample @Inject constructor(val repository: RepositoryMyCalculates): UseCase<List<MyCalculation>, Int?>(){

    override suspend fun run(params: Int?): List<MyCalculation> {
        return repository.getListCalculatesForExample()
    }
}

class LoadCalculatesList @Inject constructor(val repository: RepositoryMyCalculates): UseCase<List<MyCalculation>, Int?>(){

    override suspend fun run(params: Int?): List<MyCalculation> {
        return repository.loadList()
    }
}

class SaveOneCalculate @Inject constructor(val repository: RepositoryMyCalculates): UseCase<Boolean, MyCalculation>(){

    override suspend fun run(params: MyCalculation) :Boolean{
        val ok=repository.saveOne(params)
        Loger.log("id of saved position $ok")
        return ok!=0L
    }
}

class DeleteOneCalculate @Inject constructor(val repository: RepositoryMyCalculates): UseCase<Boolean, MyCalculation>(){

    override suspend fun run(params: MyCalculation) :Boolean{
        val ok=repository.deleteOne(params)
        Loger.log("id of saved position $ok")
        return ok>0
    }
}
