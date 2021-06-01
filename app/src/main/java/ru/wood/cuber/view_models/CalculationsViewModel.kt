package ru.wood.cuber.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.wood.cuber.data.MyCalculation
import ru.wood.cuber.repositories.RepositoryMyCalculates
import javax.inject.Inject

@HiltViewModel
class CalculationsViewModel @Inject constructor(val repository: RepositoryMyCalculates) :ViewModel() {
    var liveData = MutableLiveData<List<MyCalculation>>()

    fun getListContains(){
        liveData.value=repository.getListCalculatesForExample()
    }
}