package ru.wood.cuber.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.wood.cuber.room.AppDatabase
import ru.wood.cuber.room.DaoContains
import ru.wood.cuber.room.DaoMyCalculates
import ru.wood.cuber.room.DaoTrees
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)!!
    }

    @Provides
    fun provideDaoTrees(appDatabase: AppDatabase): DaoTrees {
        return appDatabase.daoTrees()
    }
    @Provides
    fun provideDaoMyCalculates(appDatabase: AppDatabase): DaoMyCalculates {
        return appDatabase.daoCalculates()
    }
    @Provides
    fun provideDaoContains(appDatabase: AppDatabase): DaoContains {
        return appDatabase.daoContains()
    }
}
