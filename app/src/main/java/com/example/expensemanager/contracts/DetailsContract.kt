package com.example.expensemanager.contracts

import com.example.expensemanager.ViewEvent
import com.example.expensemanager.ViewSideEffect
import com.example.expensemanager.ViewState
import com.example.expensemanager.models.Expense

class DetailsContract {
    sealed class Event : ViewEvent{
        data class AddExpense(val amount: Double, val category: String, val selectedDate: String): Event()
    }

    data class State(
        val isLoading: Boolean,
        val expenseList: List<Expense>
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data class SetResult(val result: String): Effect()
    }
}