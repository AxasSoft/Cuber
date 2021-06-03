package ru.wood.cuber.view_models

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.wood.cuber.Loger
import ru.wood.cuber.data.MyCalculatesContentsTab
import ru.wood.cuber.data.MyСontainer
import ru.wood.cuber.interactors.*
import javax.inject.Inject
@HiltViewModel
class ContainsViewModel @Inject constructor (
        private val listCase: ContainsList,
        private val save: SaveOneContain,
        private val saveContent: SaveContent,
        private val loadlist: LoadContains,
        private val deleteOne: DeleteOneContain,
        private val deleteTrees: ClearOneContain

        ) : BaseViewModel() {

    var liveData = MutableLiveData<List<MyСontainer>>()


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
        saveContent(MyCalculatesContentsTab(
                idOfCalculates = idOfCalculates,
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
}