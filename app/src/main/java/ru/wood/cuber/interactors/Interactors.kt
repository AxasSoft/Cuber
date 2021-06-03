package ru.wood.cuber.interactors

import ru.wood.cuber.data.MyCalculation
import ru.wood.cuber.data.MyСontainer
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.repositories.RepositoryContains
import ru.wood.cuber.repositories.RepositoryMyCalculates
import ru.wood.cuber.repositories.RepositoryTrees
import javax.inject.Inject

class ContainsList @Inject constructor(val repository: RepositoryContains) :UseCase<List<MyСontainer>, Int>(){

    override suspend fun run(params: Int): List<MyСontainer> {
        return repository.getListContainersForExample()
    }
}

class TreesList @Inject constructor(val repository: RepositoryTrees) :UseCase<List<TreePosition>, Int>(){

    override suspend fun run(params: Int): List<TreePosition> {
        return repository.getListTreesForExample()
    }
}

