package ru.wood.cuber.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContainerContentsTab (
        @PrimaryKey (autoGenerate = true)
        override var id:Long,
        val idOfContainer: Long,
        val idOfTreePosition: Long
) :BaseItem()