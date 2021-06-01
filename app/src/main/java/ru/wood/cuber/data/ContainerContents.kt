package ru.wood.cuber.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContainerContents (
        @PrimaryKey (autoGenerate = true)
        override var id:Int,
        val idOfContainer: Int,
        val idOfTreePosition: Int
) :BaseItem(id)