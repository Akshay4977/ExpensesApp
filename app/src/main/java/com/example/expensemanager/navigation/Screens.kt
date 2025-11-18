package com.example.expensemanager.navigation


enum class Screen {
    HOME,
    DETAILS,
    LOGIN,
    DASHBOARD,
    GRAPH
}
sealed class NavigationItem(val route: String) {
    object Home : NavigationItem(Screen.HOME.name)
    object Login : NavigationItem(Screen.LOGIN.name)
    object Dashboard : NavigationItem(Screen.DASHBOARD.name)

    object Detail : NavigationItem(Screen.DETAILS.name)
    object Graph : NavigationItem(Screen.GRAPH.name)
}


const val INDEX_PARAM = "index"
const val ROUTE = "/{$INDEX_PARAM}"
