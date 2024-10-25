package com.example.expensemanager.contracts

import com.example.expensemanager.ViewEvent
import com.example.expensemanager.ViewSideEffect
import com.example.expensemanager.ViewState
import com.example.expensemanager.models.Expense

class GraphContract {
    sealed class Event : ViewEvent {
        object Retry : Event()

        data class GetItem(val index: Int) : Event()
    }

    data class State(
        val isLoading: Boolean,
        val itemList: List<Expense>
    ) : ViewState

    sealed class Effect : ViewSideEffect
}