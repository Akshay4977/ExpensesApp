package com.example.expensemanager.usecase

import com.example.expensemanager.models.Expense
import com.example.expensemanager.repository.GetExpensesListRepository
import javax.inject.Inject

class GetExpensesListUseCase @Inject constructor(
    private val getExpensesListRepository: GetExpensesListRepository
){
    suspend fun execute(userId: Int) = getExpensesListRepository.getList(userId)

    suspend fun insert(expense: Expense) = getExpensesListRepository.insertExpense(expense)

}