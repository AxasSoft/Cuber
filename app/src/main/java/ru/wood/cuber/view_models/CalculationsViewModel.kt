package ru.wood.cuber.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.wood.cuber.data.MyCalculation
import ru.wood.cuber.interactors.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CalculationsViewModel @Inject constructor(
        private val listForExample: LoadForExample,
        private val loadlist: LoadCalculatesList,
        private val save: SaveOneCalculate,
        private val delete: DeleteOneCalculate,
        private val deleteContaines: DeleteContainerContent
        ) :BaseViewModel() {

    var liveData = MutableLiveData<List<MyCalculation>>()

    fun getExampleList(){
        listForExample(null){
            liveData.value=it
        }
    }
    fun refreshList(){
        loadlist(null){
            liveData.value=it
        }
    }

    fun addNew(name: String){
        val one = MyCalculation(
                name = name,
                date = currentDate)

        save(one){
            val ok=it
            if (ok){
                refreshList()
            }
        }
    }

    fun deletePosition(one: MyCalculation){
        delete(one){
            if (it){
                refreshList()
                clearContainersContent(one.id)
            }
        }
    }
    private fun clearContainersContent(idOfCalculates: Long){
        deleteContaines(idOfCalculates)
    }
}