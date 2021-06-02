package ru.wood.cuber.view_models

import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

abstract class BaseViewModel: ViewModel() {
    val currentDate:String
    init {
        currentDate=getDate()
    }

    private fun getDate(): String{
        val sdf = SimpleDateFormat("dd/MM/yyyy ")
        val currentDate = sdf.format(Date())
        return currentDate
    }

    //abstract fun addNew(name:String)
    //abstract fun refreshList()
}