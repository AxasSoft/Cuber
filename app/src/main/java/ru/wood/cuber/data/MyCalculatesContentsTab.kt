package ru.wood.cuber.data

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class MyCalculatesContentsTab (
        @PrimaryKey(autoGenerate = true)
        override var id: Long=0,
        val idOfCalculates: Long?,
        val idOfContainers: Long?
        ):BaseItem()