package ru.wood.cuber.view_models

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.wood.cuber.Loger
import ru.wood.cuber.data.*
import ru.wood.cuber.interactors.*
import ru.wood.cuber.interactors.ParamsClasses.Limit
import ru.wood.cuber.interactors.ParamsClasses.NewParams
import javax.inject.Inject
@HiltViewModel
class TreesViewModel @Inject constructor (
        private val listCase: TreesList,
        private val save: SaveOneTree,
        private val saveList: SaveListTree,
        private val saveContent: SaveTreeContent,
        private val saveMoreContent: SaveTreeContentList,
        private val loadlist: LoadTrees,
        private val deleteOne: DeleteOneTree,
        private val updateLength: UpdateTreeLength,
        private val updateParams: UpdateTreePositions,
        private val getOne: OnePositionById,
        private val deleteByLimit: DeleteByLimit,
        private val getPosiitonList:GetPositionsList
        ) : BaseViewModel () {
    var liveData = MutableLiveData<List<TreePosition>>()
    var onePositionLiveData = MutableLiveData<TreePosition>()
    var paramsIsSaved= MutableLiveData<Boolean>()

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
                saveContent(
                        idOfContain= commonСontainerId!!,
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

    fun changeCommonLength(length : Double, idOfContain:  Long?){
        val newLength = NewParams(commonСontainerId!!, length)
        updateLength(newLength){
            if (it){
                refreshList(idOfContain!!)
            }
        }
    }

    fun getOneTree(id: Long){
        getOne(id){
            onePositionLiveData.value=it
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
            Loger.log("ContainerContentsTab List  $list")
            if (it){
                //quantityChangingLiveData.value=true
                refreshList(idOfContain)
            }
        }
    }

    fun limitDelete(diameter: Int, length: Double, limit: Int){
        val limit=Limit(diameter, length, limit)
        deleteByLimit(limit){
            if (it){
                //quantityChangingLiveData.value=true
                refreshList(commonСontainerId!!)
            }
        }
    }

    fun addPositions(count: Int, length: Double, diameter: Int){
        val list: MutableList<TreePosition> = ArrayList()
        for (x in 1..count){
            list.add(TreePosition(length = length, diameter =diameter ))
        }

        saveList(list){
            val ok= it.isNotEmpty()
            if (ok){
                saveContentList(idOfContain =commonСontainerId!!,
                                idList = it)
            }
        }
    }
    fun saveNewParams(
            lastLength: Double,
            lastdiameter: Int,
            newLength: Double,
            newDiameter: Int){
        val lastParams= NewParams(
                containerOfTrees =commonСontainerId!!,
                 length=lastLength,
                diameter=lastdiameter
        )
        getPosiitonList(lastParams){
            Loger.log("${lastParams.containerOfTrees} +${lastParams.length}+ ${lastParams.diameter!!}  ☻☻☻☻▐---------------")
            Loger.log(it.size.toString()+" ☻☻☻☻▐---------------")

            val newParams=NewParams(
                    containerOfTrees=commonСontainerId!!,
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
}