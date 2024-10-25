package com.example.expensemanager.repository

import com.example.expensemanager.dao.ExpensesDao
import com.example.expensemanager.models.Expense
import javax.inject.Inject

class GetExpensesListRepositoryImplementation @Inject constructor(private val expensesDao: ExpensesDao) :
    GetExpensesListRepository {
    override suspend fun getList(userId : Int): List<Expense> {
        return expensesDao.getAllExpenses()
    }

    override suspend fun insertExpense(expense: Expense) {
         expensesDao.insertExpense(expense)
    }

}