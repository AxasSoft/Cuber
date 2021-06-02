package ru.wood.cuber.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TreePosition (
    @PrimaryKey(autoGenerate = true)
    override var id: Long,
    val length: Double?,
    val diameter: Double?,
    val quantity: Int?,
    val volume: Double?
):BaseItem()