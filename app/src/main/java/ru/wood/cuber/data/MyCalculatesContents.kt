package ru.wood.cuber.data

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class MyCalculatesContents (
        @PrimaryKey(autoGenerate = true)
        override var id: Int,
        val idOfCalculates: Int,
        val idOfContainers: Int
        ):BaseItem(id)