package com.example.expensemanager.di

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.expensemanager.dao.ExpensesDao
import com.example.expensemanager.models.Expense

@Database(
    entities = [
        Expense::class
    ],
    exportSchema = true,
    version = VERSION,
    )

abstract class AppDatabase : RoomDatabase() {

    abstract fun recentSearchFlightsDao(): ExpensesDao
}

const val VERSION = 1