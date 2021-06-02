package ru.wood.cuber.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.wood.cuber.repositories.RepositoryTrees
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.interactors.TreesList
import javax.inject.Inject
@HiltViewModel
class TreesViewModel @Inject constructor (val listCase: TreesList) : BaseViewModel () {
    var liveData = MutableLiveData<List<TreePosition>>()

    fun getListTrees(idContainer: Int){
        listCase(idContainer){
            liveData.value=it
        }
    }

    fun addNew(name: String) {

    }

    fun refreshList() {
    }
}