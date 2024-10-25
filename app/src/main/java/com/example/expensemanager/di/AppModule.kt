package com.example.expensemanager.di

import android.app.Application
import com.example.expensemanager.usecase.GetApplicationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun getApplication(context: Application): GetApplicationUseCase {
        return GetApplicationUseCase(context)
    }
}