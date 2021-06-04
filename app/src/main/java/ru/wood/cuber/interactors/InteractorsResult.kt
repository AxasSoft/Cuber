package ru.wood.cuber.interactors

import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.repositories.RepositoryResult
import ru.wood.cuber.repositories.RepositoryTrees
import javax.inject.Inject


class LoadResult @Inject constructor(val repository: RepositoryResult): UseCase<List<TreePosition>, Long>(){

    override suspend fun run(params: Long): List<TreePosition> {
        return repository.loadResult(params)
    }
}