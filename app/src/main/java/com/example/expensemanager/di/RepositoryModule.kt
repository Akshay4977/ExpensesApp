package com.example.expensemanager.di

import com.example.expensemanager.dao.ExpensesDao
import com.example.expensemanager.networking.ApiService
import com.example.expensemanager.repository.GetExpensesListRepository
import com.example.expensemanager.repository.GetExpensesListRepositoryImplementation
import com.example.expensemanager.repository.GetListRepository
import com.example.expensemanager.repository.GetListRepositoryImplementation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideGetListRepository(apiService: ApiService): GetListRepository {
        return GetListRepositoryImplementation(apiService = apiService)
    }

    @Singleton
    @Provides
    fun provideExpensesRepository(
        expensesDao: ExpensesDao
    ): GetExpensesListRepository {
        return GetExpensesListRepositoryImplementation(expensesDao = expensesDao)
    }
}
