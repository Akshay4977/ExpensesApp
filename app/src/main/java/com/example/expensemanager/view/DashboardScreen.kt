package com.example.expensemanager.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.expensemanager.navigation.NavigationViewModel
import com.example.expensemanager.viewmodels.HomeViewModel

@Composable
fun Dashboard(navigationViewModel: NavigationViewModel, homeViewModel: HomeViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text("welcome")
    }
}