package com.example.expensemanager.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.example.expensemanager.BaseViewModel
import com.example.expensemanager.contracts.HomeContract
import com.example.expensemanager.usecase.GetExpensesListUseCase
import com.example.expensemanager.usecase.GetItemUseCase
import com.example.expensemanager.usecase.GetListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow as Flow1

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

    init {

        val a =  10
        //Log.e("inside","->"+a.add())
        temp()
    }


    /*
    * write an extension function "unique" to emit only distinct consecutive values

flowOf(1, 2, 2, 2, 1, 1, 3, 3, 4)
    .unique()
    .collect { println(it) }


Output : 1, 2 , 1, 3, 4
    * */
    /*Problem Statement:
You need to simulate a Producer-Consumer scenario using Kotlin coroutines, where:

- Producers generate random integers (between 1 and 100) and add them to the buffer - once a secong - 10 times .
- Consumers retrieve integers from the buffer and process them once per 2 second.
- consumer should consume all produced numbers not lose any of them
    * */

/*
    private fun temp1(){
        flowOf(1, 2, 2, 2, 1, 1, 3, 3, 4).temp().collect { println(it) }

    }


    fun Flow1.temp(): Flow1<Int> {
        return flowOf(1)
    }
*/


    fun String.sort() : String{
        return ""
    }

    private fun Int.add() : Int {
        return this  *  this
    }

    private fun temp() {
         val channel = Channel<Int>()

        viewModelScope.launch {
                var counter = 0
                while (counter < 10) {
                    delay(1000) // Send data every second
                    channel.send(counter++)
                    Log.e("inside",": $counter")
                }
            }


            viewModelScope.launch {
                while (channel.isClosedForReceive) {
                    val data = channel.receive() // Receive data
                    Log.e("inside",": $data")
                    delay(2000) // Process data every alternate second
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
