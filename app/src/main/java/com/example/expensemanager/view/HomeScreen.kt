package com.example.expensemanager.view

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensemanager.R
import com.example.expensemanager.SIDE_EFFECT_KEY
import com.example.expensemanager.contracts.HomeContract
import com.example.expensemanager.models.Expense
import com.example.expensemanager.navigation.NavigationContract
import com.example.expensemanager.navigation.NavigationViewModel
import com.example.expensemanager.ui.theme.ExpenseManagerTheme
import com.example.expensemanager.viewmodels.HomeViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navigationViewModel: NavigationViewModel, homeViewModel: HomeViewModel) {

    val state = homeViewModel.viewState.value
    val dataList = state.itemList
    val effectFlow = homeViewModel.effect
    val userId = 2
    val income = remember {
        mutableStateOf(1000.0)
    }
    LaunchedEffect(SIDE_EFFECT_KEY) {

        merge(effectFlow).onEach { effect ->
            when (effect) {
                is HomeContract.Effect.NavigateToDetailsScreen -> {
                    Log.e("inside", "NavigateToDetailsScreen->" + effect.index)
                    navigationViewModel.setEffect {
                        NavigationContract.Effect.Navigation.ToDetailsScreen(effect.index)
                    }
                }
            }
        }.collect()
    }


    LaunchedEffect(Unit) {
        homeViewModel.getResult(userId)
    }

    ScrollableLayout(list = dataList, income = income.value, onAddClick = {
        navigationViewModel.setEffect {
            NavigationContract.Effect.Navigation.ToDetailsScreen(userId)
        }
    },onGraphClick = {
        Log.e("inside", "ToGraphScreen->" )
        navigationViewModel.setEffect {
            NavigationContract.Effect.Navigation.ToGraphScreen(userId)
        }
    })
}

@Composable
fun ItemDesign(expense: Expense) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(text = "$" + expense.price.toString(), textAlign = TextAlign.Start)

            Text(text = expense.date, textAlign = TextAlign.End)
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, bottom = 8.dp),
            text = expense.category, textAlign = TextAlign.Start
        )
    }
}


@Composable
fun ScrollableLayout(list: List<Expense>, onAddClick: () -> Unit, onGraphClick: () -> Unit,  income: Double) {
    val spending = list.sumOf { it.price }
    val balance = income - spending
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // Top
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.purple_500))
                .clickable {
                    onGraphClick()
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = "Balance : $" + balance,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {

                Text(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(vertical = 4.dp),
                    text = "Spending : $" + spending,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold, color = Color.White

                )

                Text(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(vertical = 4.dp),
                    text = "Income : S" + income,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold, color = Color.White
                )
            }
        }

        // Scrollable middle section
        Box(modifier = Modifier
            .weight(1f)
            .padding(top = 16.dp)) {
            if (list.isNotEmpty()) {
                LazyColumn {
                    itemsIndexed(list) { index, item ->
                        ItemDesign(item)
                    }
                }
            }
        }


        // Bottom button
        Button(
            onClick = { onAddClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonColors(
                containerColor = colorResource(id = R.color.purple_500),
                contentColor = Color.White,
                disabledContentColor = Color.White,
                disabledContainerColor = Color.White,
            )
        ) {
            Text("Add New Expense", fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Temp() {
    ExpenseManagerTheme {
        ScrollableLayout(list = listOf(Expense(1, 2, 100.0, "food", "10/10/2024")), onAddClick = {}, onGraphClick = {}, income = 1000.0)
    }
}
