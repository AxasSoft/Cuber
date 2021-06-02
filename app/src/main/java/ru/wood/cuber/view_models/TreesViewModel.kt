package ru.wood.cuber.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.wood.cuber.Loger
import ru.wood.cuber.data.ContainerContentsTab
import ru.wood.cuber.data.MyCalculatesContentsTab
import ru.wood.cuber.data.MyСontainer
import ru.wood.cuber.repositories.RepositoryTrees
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.interactors.*
import javax.inject.Inject
@HiltViewModel
class TreesViewModel @Inject constructor (
        private val listCase: TreesList,
        private val save: SaveOneTree,
        private val saveContent: SaveTreeContent,
        private val loadlist: LoadTrees,
        private val deleteOne: DeleteOneTree,
        ) : BaseViewModel () {
    var liveData = MutableLiveData<List<TreePosition>>()

    var commonСontainerId: Long?=null
    var commonLength: Double?=null

    fun getListTrees(idContainer: Int){
        listCase(idContainer){
            liveData.value=it
        }
    }


    fun refreshList(idOfContain: Long){
        loadlist(idOfContain){
            Loger.log("refresh list size ${it.size}")
            liveData.value=it
        }
    }

    fun addNew(diameter: Int?){
        val one = TreePosition(
                length = commonLength!!,
                diameter = diameter
        )

        save(one){
            val ok=it!=0L
            if (ok){
                saveContent(idOfContain= commonСontainerId!!, idOfTree= it)
            }
        }
    }

    private fun saveContent(idOfContain: Long, idOfTree: Long){
        saveContent(ContainerContentsTab(
                idOfContainer = idOfContain,
                idOfTreePosition = idOfTree
        )){ contentSaved ->
            if(contentSaved){
                refreshList(idOfContain)
            }
        }
    }

    fun deltePosition (one : TreePosition, idOfContain:  Long){
        deleteOne(one){
            if (it){
                refreshList(idOfContain)
            }
        }
    }
}