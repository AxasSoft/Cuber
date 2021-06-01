package ru.wood.cuber.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.wood.cuber.repositories.RepositoryTrees
import ru.wood.cuber.data.TreePosition
import javax.inject.Inject
@HiltViewModel
class TreesViewModel @Inject constructor (val repository: RepositoryTrees) : ViewModel () {
    var liveData = MutableLiveData<List<TreePosition>>()

    fun getListTrees(idContainer: Int){
        liveData.value=repository.getListTreesForExample()
    }
}