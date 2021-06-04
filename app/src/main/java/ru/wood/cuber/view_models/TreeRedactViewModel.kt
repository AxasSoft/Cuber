package ru.wood.cuber.view_models

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.wood.cuber.Loger
import ru.wood.cuber.data.ContainerContentsTab
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.interactors.*
import ru.wood.cuber.interactors.ParamsClasses.Limit
import ru.wood.cuber.interactors.ParamsClasses.NewParams
import javax.inject.Inject

@HiltViewModel
class TreeRedactViewModel @Inject constructor(
        private val saveList: SaveListTree,
        private val updateParams: UpdateTreePositions,
        private val getOne: OnePositionById,
        private val deleteByLimit: DeleteByLimit,
        private val getPosiitonList: GetPositionsList,
        private val saveMoreContent: SaveTreeContentList,

        ) : BaseViewModel() {
    var onePositionLiveData = MutableLiveData<TreePosition>()
    var paramsIsSaved= MutableLiveData<Boolean>()
    var redactFinished= MutableLiveData<Boolean>()

    fun getOneTree(id: Long){
        getOne(id){
            onePositionLiveData.value=it
        }
    }

    fun saveNewParams(
            container:Long,
            lastLength: Double,
            lastdiameter: Int,
            newLength: Double,
            newDiameter: Int){
        val lastParams= NewParams(
                containerOfTrees =container,
                length=lastLength,
                diameter=lastdiameter
        )
        getPosiitonList(lastParams){
            Loger.log("${lastParams.containerOfTrees} +${lastParams.length}+ ${lastParams.diameter!!}  ☻☻☻☻▐---------------")
            Loger.log(it.size.toString()+" ☻☻☻☻▐---------------")

            val newParams= NewParams(
                    containerOfTrees=container,
                    length = newLength,
                    diameter = newDiameter,
                    idList = it
            )
            update(newParams)
        }
    }

    private fun update(newParams: NewParams){
        updateParams(newParams){
            if (it){
                Loger.log("paramsIsSaved.value=true ---------------------------")
                paramsIsSaved.value=true
                //refreshList(commonСontainerId!!)

            }
        }
    }

    fun addPositions(
            container:Long,count: Int, length: Double, diameter: Int){
        val list: MutableList<TreePosition> = ArrayList()
        for (x in 1..count){
            list.add(TreePosition(length = length, diameter =diameter ))
        }

        saveList(list){
            Loger.log(it)
            val ok= it.isNotEmpty()
            if (ok){
                Loger.log("container $container")
                saveContentList(idOfContain =container,
                        idList = it)
            }
        }
    }
    fun limitDelete(container: Long, diameter: Int, length: Double, limit: Int){
        val limit= Limit(container, diameter, length, limit)
        deleteByLimit(limit){
            if (it){
                Loger.log("$it \n limit $limit")
                redactFinished.value=true
                //refreshList(container)
            }
        }
    }
    private fun saveContentList(idOfContain: Long,idList: List<Long>){
        val list : MutableList<ContainerContentsTab> = ArrayList()
        for (id in idList){
            Loger.log("id in idList $id")
            list.add(ContainerContentsTab(
                    idOfContainer = idOfContain,
                    idOfTreePosition = id
            )
            )
        }
        saveMoreContent(list){
            Loger.log(it)
            Loger.log("ContainerContentsTab List  $list")
            if (it){
                redactFinished.value=true
                //refreshList(idOfContain)
            }
        }
    }
}