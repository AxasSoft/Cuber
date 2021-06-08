package ru.wood.cuber.view_models

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.wood.cuber.Loger
import ru.wood.cuber.data.*
import ru.wood.cuber.interactors.*
import ru.wood.cuber.interactors.ParamsClasses.Limit
import ru.wood.cuber.interactors.ParamsClasses.NewParams
import ru.wood.cuber.volume.Volume
import java.util.*
import javax.inject.Inject
@HiltViewModel
class TreesViewModel @Inject constructor (
        private val save: SaveOneTree,
        private val saveContent: SaveTreeContent,
        private val loadlist: LoadTrees,
        private val deleteOne: DeleteOneTree,
        private val updateLength: UpdateTreeLength,
        private val loadOneContainer:LoadOne,
        private val simpleiList:SimpleLoadTrees,
        private val updateVolume:UpdateVolume
        ) : BaseViewModel () {
    var liveData = MutableLiveData<List<TreePosition>>()
    var containerLive = MutableLiveData<MyСontainer>()
    var diameters : MutableList<Int> = arrayListOf()

    var commonLength: Double?=null

    fun refreshList(idOfContain: Long){
        loadlist(idOfContain){
            Loger.log("refresh list size ${it.size}")
            Collections.sort(it, kotlin.Comparator { o1, o2 -> o1.id.compareTo(o2.id) })


            liveData.value=it
        }
    }
    
    fun addNew(container: Long, diameter: Int?, volume: Double){
        val one = TreePosition(
                length = commonLength!!,
                diameter = diameter,
            volume=volume
        )

        save(one){
            val ok=it!=0L
            if (ok){
                if (!diameters.contains(diameter)){
                    diameters.add(diameter!!)
                }
                saveContent(
                        idOfContain= container,
                        idOfTree= it)
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

    fun deletePosition (one : TreePosition, idOfContain:  Long){
        deleteOne(one){
            if (it){
                refreshList(idOfContain)
            }
        }
    }

    fun changeCommonLength(length : Double, container:  Long){
        val newLength = NewParams(container, length)
        updateLength(newLength){
            if (it){
                changeVolumes(container,length)
                refreshList(container)
            }
        }
    }
    private fun changeVolumes(currentContainer: Long, newLength: Double){
        for (diametr in diameters){
            changeVolumes(currentContainer,diametr,newLength)
        }
    }

    private fun changeVolumes(currentContainer: Long, diametr: Int, newLength: Double){
        val newVolume=Volume.calculateOne(diametr,newLength,1)
        val newParam = NewParams(currentContainer, newLength,volume = newVolume)
        updateVolume(newParam){
            println("update volume for $it positions is finished")
        }
    }

    fun loadContainer (id: Long){
        loadOneContainer(id){
            containerLive.value=it
        }
    }

    suspend fun loadList(container: Long): List<TreePosition>{
        return simpleiList.run(container)
    }
}