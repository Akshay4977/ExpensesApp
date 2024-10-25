package com.example.expensemanager.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.example.expensemanager.BaseViewModel
import com.example.expensemanager.contracts.GraphContract
import com.example.expensemanager.usecase.GetExpensesListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class GraphViewModel @Inject constructor(
    private val getExpensesListUseCase: GetExpensesListUseCase
) : BaseViewModel<GraphContract.Event, GraphContract.State, GraphContract.Effect>() {
    override fun setInitialState() = GraphContract.State(
        isLoading = false,
        itemList = emptyList()
    )

    override fun handleEvents(event: GraphContract.Event) {
        when (event) {
            is GraphContract.Event.Retry -> {}

            is GraphContract.Event.GetItem -> {
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getResult(userId: Int) {
        setState {
            copy(isLoading = true)
        }

        viewModelScope.launch {
            val list = getExpensesListUseCase.execute(userId = userId)
            val result = list.filter { trimDate(it.date) == getMonth() }
            Log.e("inside","result->"+result)
            setState {
                copy(isLoading = false, itemList = result)
            }
        }
    }

    fun trimDate(date: String): String {
        val result = date.split("/").toTypedArray()
        return result[1]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMonth(): String {
        val formatter = DateTimeFormatter.ofPattern("MM")
        val current = LocalDateTime.now().format(formatter)
        return current
    }
}
