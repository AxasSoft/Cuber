package ru.wood.cuber.data

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class MyCalculation (
    @PrimaryKey (autoGenerate = true)
    override var id: Int,
    val name: String,
    val date:String,
    val quantity: Int // ??????????
        ):BaseItem(id)