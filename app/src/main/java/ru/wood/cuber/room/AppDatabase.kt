package ru.wood.cuber.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.wood.cuber.data.*

const val DATABASE_NAME = "cuber-db"

@Database (entities = [
    ContainerContentsTab::class,
    MyCalculatesContentsTab::class,
    TreePosition::class,
    MyСontainer::class,
    MyCalculation::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun daoTrees(): DaoTrees
    abstract fun daoCalculates(): DaoMyCalculates
    abstract fun daoContains(): DaoContains

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DATABASE_NAME)
                            .fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}