package ru.wood.cuber.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import ru.wood.cuber.Loger
import ru.wood.cuber.data.MyOrderContentsTab
import ru.wood.cuber.data.MyСontainer
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.databinding.ItemContainerSwipeBinding
import ru.wood.cuber.interactors.*
import javax.inject.Inject
@HiltViewModel
class ContainsViewModel @Inject constructor (
        private val save: SaveOneContain,
        private val saveContent: SaveContent,
        private val loadlist: LoadContains,
        private val deleteOne: DeleteOneContain,
        private val deleteTrees: ClearOneContain,
        private val getCommonQuantity:CommonQuantity,
        private val simpleiList:SimpleLoadTrees
        ) : BaseViewModel() {

    var liveData = MutableLiveData<List<MyСontainer>>()
    var commonQuantity = MutableLiveData<Int>()

    fun refreshList(idOfCalculates: Long){
        loadlist(idOfCalculates){
            Loger.log("refresh list size ${it.size}")
            liveData.value=it
        }
    }

    fun addNew(name: String, idOfCalculates: Long){
        val one = MyСontainer(
                name = name,
                date = currentDate)

        save(one){
            val ok=it!=0L
            if (ok){
                saveContent(idOfCalculates=idOfCalculates, idOfContainers=it)
            }
        }
    }

    private fun saveContent(idOfCalculates: Long, idOfContainers: Long){
        saveContent(MyOrderContentsTab(
                idOfOrder = idOfCalculates,
                idOfContainers = idOfContainers
        )){ contentSaved ->
            if(contentSaved){
                refreshList(idOfCalculates)
            }
        }
    }

    fun deltePosition (one :MyСontainer, idOfCalculates: Long){
        deleteOne(one){
            if (it){
                refreshList(idOfCalculates)
                clearTreesContent(one.id)
            }
        }
    }
    private fun clearTreesContent(idOfContainers: Long){
        deleteTrees(idOfContainers)
    }

    suspend fun getQuantity(id: Long): Int{
        return getCommonQuantity.run(id)
    }
    suspend fun loadListTrees(container: Long): List<TreePosition>{
        return simpleiList.run(container)
    }
}