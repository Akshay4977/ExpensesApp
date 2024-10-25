package com.example.expensemanager.repository

import com.example.expensemanager.models.Expense

interface GetExpensesListRepository {

    suspend fun getList(userId: Int): List<Expense>

    suspend fun insertExpense(expense: Expense)

}