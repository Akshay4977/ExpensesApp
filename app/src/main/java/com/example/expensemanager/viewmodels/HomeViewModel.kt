package com.example.expensemanager.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.example.expensemanager.BaseViewModel
import com.example.expensemanager.contracts.HomeContract
import com.example.expensemanager.usecase.GetExpensesListUseCase
import com.example.expensemanager.usecase.GetItemUseCase
import com.example.expensemanager.usecase.GetListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getListUseCase: GetListUseCase,
    private val getItemUseCase: GetItemUseCase,
    private val getExpensesListUseCase: GetExpensesListUseCase
) : BaseViewModel<HomeContract.Event, HomeContract.State, HomeContract.Effect>() {
    override fun setInitialState() = HomeContract.State(
        isLoading = false,
        itemList = emptyList()
    )

    override fun handleEvents(event: HomeContract.Event) {
        when (event) {
            is HomeContract.Event.Retry -> {}

            is HomeContract.Event.GetItem -> {
                getItem(event.index)
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
            setState {
                copy(isLoading = false, itemList = result)
            }
        }
    }

    fun getItem(index: Int) {
        setEffect {
            HomeContract.Effect.NavigateToDetailsScreen(index)
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
