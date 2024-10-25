package com.example.expensemanager.di

import android.content.Context
import androidx.room.Room
import com.example.expensemanager.dao.ExpensesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, DATABASE_NAME
        ).build()
    }


    @Provides
    fun provideExpensesDao(appDatabase: AppDatabase): ExpensesDao{
        return appDatabase.recentSearchFlightsDao()
    }
}


private const val DATABASE_NAME = "expenses-database"