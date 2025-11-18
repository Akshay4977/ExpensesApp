package com.example.expensemanager.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.expensemanager.view.Dashboard
import com.example.expensemanager.view.DetailsScreen
import com.example.expensemanager.view.GraphScreen
import com.example.expensemanager.view.HomeScreen
import com.example.expensemanager.view.LoginScreen
import com.example.expensemanager.viewmodels.DetailsViewModel
import com.example.expensemanager.viewmodels.GraphViewModel
import com.example.expensemanager.viewmodels.HomeViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navigationViewModel = hiltViewModel<NavigationViewModel>()
    val navController = rememberNavController()
    var startDestination by remember {
        mutableStateOf(NavigationItem.Login.route)
    }

    val effectFlow = navigationViewModel.effect.collectAsState(initial = null)
    effectFlow.value?.let {
        handleEffect(navController, it) { route ->
            startDestination = route
        }

        navigationViewModel.setEffect { NavigationContract.Effect.Navigation.Reset }
    }

    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationItem.Home.route) {
            val homeViewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(navigationViewModel, homeViewModel)
        }

        composable(NavigationItem.Login.route) {
            val homeViewModel = hiltViewModel<HomeViewModel>()
            LoginScreen(navigationViewModel, homeViewModel)
        }

        composable(NavigationItem.Dashboard.route) {
            val homeViewModel = hiltViewModel<HomeViewModel>()
            Dashboard(navigationViewModel, homeViewModel)
        }

        composable(
            route = NavigationItem.Detail.route + ROUTE,
            arguments = listOf(navArgument(INDEX_PARAM) {
                type = NavType.IntType
            })
        ) {
            val detailsViewModel = hiltViewModel<DetailsViewModel>()
            val index = it.arguments?.getInt(INDEX_PARAM)
            index?.let { index ->
                DetailsScreen(onEventSent = { event -> detailsViewModel.setEvent(event)}, navigationViewModel, index, detailsViewModel )
            }
        }

        composable(
            route = NavigationItem.Graph.route + ROUTE,
            arguments = listOf(navArgument(INDEX_PARAM) {
                type = NavType.IntType
            })
        ) {
            val graphViewModel = hiltViewModel<GraphViewModel>()
            val index = it.arguments?.getInt(INDEX_PARAM)
            index?.let { index ->
                GraphScreen(onEventSent = { event -> graphViewModel.setEvent(event)}, navigationViewModel, index, graphViewModel )
            }
        }
    }
}

private fun handleEffect(
    navController: NavController,
    effect: NavigationContract.Effect,
    callBack: (String) -> Unit
) {
    MainScope().launch {
        when (effect) {

            is NavigationContract.Effect.Navigation.ToDetailsScreen -> {
                Log.e("inside", "index->" + effect.index)
                navController.navigate(NavigationItem.Detail.route + "/" + effect.index)
            }

            is NavigationContract.Effect.Navigation.ToGraphScreen -> {
                Log.e("inside", "index->" + effect.index)
                navController.navigate(NavigationItem.Graph.route + "/" + effect.index)
            }

            is NavigationContract.Effect.Navigation.PopBackStack -> {
                Log.e("inside", "here")
                navController.popBackStack()
            }

            is NavigationContract.Effect.Navigation.ToDashboard -> {
                navController.navigate(NavigationItem.Dashboard.route)
            }

            is NavigationContract.Effect.Navigation.ToLoginScreen -> {
                navController.navigate(NavigationItem.Login.route)
            }

            is NavigationContract.Effect.Navigation.Reset -> {

            }
        }
    }
}
