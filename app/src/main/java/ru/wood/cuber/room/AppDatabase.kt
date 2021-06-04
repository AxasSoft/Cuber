package ru.wood.cuber.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.wood.cuber.data.*

const val DATABASE_NAME = "cuber-db"

@Database (entities = [
    ContainerContentsTab::class,
    MyOrderContentsTab::class,
    TreePosition::class,
    MyСontainer::class,
    MyOrder::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun daoTrees(): DaoTrees
    abstract fun daoOrder(): DaoMyOrder
    abstract fun daoContains(): DaoContains
    abstract fun daoResults(): DaoResults
    abstract fun daoContainRedact(): DaoContainRedact

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