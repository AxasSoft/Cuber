package ru.wood.cuber.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.wood.cuber.data.MyСontainer
import ru.wood.cuber.repositories.RepositoryContains
import javax.inject.Inject
@HiltViewModel
class ContainsViewModel @Inject constructor (val repository: RepositoryContains) : ViewModel() {
    var liveData = MutableLiveData<List<MyСontainer>>()

    fun getListContains(id: Int){
        liveData.value=repository.getListContainersForExample()
    }
}