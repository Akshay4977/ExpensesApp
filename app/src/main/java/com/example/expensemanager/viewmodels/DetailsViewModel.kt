package com.example.expensemanager.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.expensemanager.BaseViewModel
import com.example.expensemanager.contracts.DetailsContract
import com.example.expensemanager.models.Expense
import com.example.expensemanager.usecase.GetExpensesListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getExpensesListUseCase: GetExpensesListUseCase
) : BaseViewModel<DetailsContract.Event, DetailsContract.State, DetailsContract.Effect>() {
    override fun setInitialState() = DetailsContract.State(
        isLoading = false,
        expenseList = emptyList()
    )

    override fun handleEvents(event: DetailsContract.Event) {
        when (event) {
            is DetailsContract.Event.AddExpense -> {
                addExpense(event.amount, event.category, event.selectedDate)
            }

            else -> {}
        }
    }


    private fun addExpense(price: Double, category: String, selectedDate: String) {
        if(price == 0.0 || category.isEmpty() || selectedDate.isEmpty()){
            setEffect {
                DetailsContract.Effect.SetResult("Enter valid data")
            }
        } else {

            val expense = Expense(userId = 2, price = price, category = category, date = selectedDate)
            viewModelScope.launch {
                getExpensesListUseCase.insert(expense)
                setEffect {
                    DetailsContract.Effect.SetResult("Data added successfully...")
                }
            }
        }
    }

    fun getItem(userId: Int) {
        setState { copy(isLoading = true) }

        viewModelScope.launch {
            val list = getExpensesListUseCase.execute(userId)
            list.forEach {
                Log.e("inside", "->" + it)
            }
            setState {
                copy(isLoading = false, expenseList = list)
            }
        }
    }
}
