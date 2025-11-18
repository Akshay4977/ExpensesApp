package com.example.expensemanager.navigation

import com.example.expensemanager.ViewEvent
import com.example.expensemanager.ViewSideEffect
import com.example.expensemanager.ViewState

class NavigationContract {

    sealed class Event : ViewEvent

     class State(
        val isLoading : Boolean
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect() {
            data class ToDetailsScreen(val index: Int) : Navigation()

            data class ToGraphScreen(val index: Int) : Navigation()

            object ToLoginScreen : Navigation()

            object ToDashboard : Navigation()

            object PopBackStack : Navigation()

            object Reset : Navigation()
        }
    }
}
