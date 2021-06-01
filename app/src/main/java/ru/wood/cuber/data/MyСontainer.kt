package ru.wood.cuber.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MyСontainer (
    @PrimaryKey(autoGenerate = true)
    override var id:Int,
    val date: String,
    val name:String,
    val volume: Double,
    val quantity: Int
):BaseItem(id)
