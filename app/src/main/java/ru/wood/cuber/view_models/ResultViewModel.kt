package ru.wood.cuber.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.interactors.LoadResult
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val loadResult: LoadResult
):BaseViewModel()  {
    var liveData = MutableLiveData<List<TreePosition>> ()

    fun getListPosition(containerId: Long){
        loadResult(containerId){
            liveData.value=it
        }
    }
}