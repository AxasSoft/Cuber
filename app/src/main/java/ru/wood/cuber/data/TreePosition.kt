package ru.wood.cuber.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TreePosition (
    @PrimaryKey(autoGenerate = true)
    override var id: Long=0,
    val length: Double?,
    val diameter: Int?,
    val quantity: Int?=null,
    val volume: Double?=null
):BaseItem()