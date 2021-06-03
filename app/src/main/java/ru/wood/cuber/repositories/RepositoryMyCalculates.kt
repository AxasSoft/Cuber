package ru.wood.cuber.repositories

import ru.wood.cuber.data.MyCalculation
import ru.wood.cuber.room.DaoMyCalculates
import javax.inject.Inject

class RepositoryMyCalculates @Inject constructor(val dao: DaoMyCalculates)  {

    fun getListCalculatesForExample(): List<MyCalculation> {
        val container1 = MyCalculation(1, "Расчет №1", "22.05.2021",5)
        val container2 = MyCalculation(2, "Расчет №2", "22.05.2021",1)
        val container3 = MyCalculation(3, "Расчет №3", "22.05.2021",2)
        val container4 = MyCalculation(4, "Расчет №4", "22.05.2021",4)
        val container5 = MyCalculation(5, "Расчет №5", "22.05.2021",5)

        val list: List<MyCalculation> =
            arrayListOf(container1,container2,container3,container4,container5)
        return list
    }

    fun saveOne(one: MyCalculation): Long{
        return dao.save(one)
    }

    fun loadList() : List<MyCalculation>{
        return dao.load()
    }

    fun  deleteOne(one: MyCalculation): Int{
        return dao.delete(one)
    }
}