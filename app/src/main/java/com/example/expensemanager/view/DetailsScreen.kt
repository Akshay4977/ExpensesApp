package com.example.expensemanager.view

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensemanager.R
import com.example.expensemanager.SIDE_EFFECT_KEY
import com.example.expensemanager.contracts.DetailsContract
import com.example.expensemanager.navigation.NavigationViewModel
import com.example.expensemanager.ui.theme.ExpenseManagerTheme
import com.example.expensemanager.viewmodels.DetailsViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import java.util.Calendar
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    onEventSent: (event: DetailsContract.Event) -> Unit,
    navigationViewModel: NavigationViewModel,
    userId: Int,
    detailsViewModel: DetailsViewModel
) {

    LaunchedEffect(Unit) {
        //detailsViewModel.getItem(userId)
    }
    val effectFlow = detailsViewModel.effect
    var result by remember { mutableStateOf("") }
    LaunchedEffect(SIDE_EFFECT_KEY) {

        merge(effectFlow).onEach { effect ->
            when (effect) {
                is DetailsContract.Effect.SetResult -> {
                    result = effect.result
                }
            }
        }.collect()
    }


    var amount by remember { mutableStateOf(0.0) }
    val list = detailsViewModel.viewState.value.expenseList

    var mExpanded by remember { mutableStateOf(false) }
    val categories = listOf("Food", "Travel", "Bills", "Rent")
    var mSelectedText by remember { mutableStateOf("") }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }
    val mDate = remember { mutableStateOf("") }

    if (result.isNotEmpty()) {
        Toast.makeText(LocalContext.current, result, Toast.LENGTH_SHORT).show()
        result = ""
        amount = 0.0
        mDate.value = ""
        mSelectedText = ""
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            TextField(
                value = amount.toString(),
                onValueChange = { newText ->
                    amount = newText.toDouble()
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
                label = { Text("Enter Amount") },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .background(Color.White)
            )


            val icon = if (mExpanded)
                Icons.Filled.KeyboardArrowUp
            else
                Icons.Filled.KeyboardArrowDown

            Column {
                OutlinedTextField(
                    value = mSelectedText,
                    onValueChange = {
                        mSelectedText = it
                        Log.e("inside", "->" + it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            mTextFieldSize = coordinates.size.toSize()
                        },
                    label = { Text("Category") },
                    trailingIcon = {
                        Icon(icon, "contentDescription",
                            Modifier.clickable { mExpanded = !mExpanded })
                    }
                )

                DropdownMenu(
                    expanded = mExpanded,
                    onDismissRequest = { mExpanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
                ) {
                    categories.forEach { label ->
                        DropdownMenuItem(text = { DropdownMenuItemText(label) }, onClick = {
                            mSelectedText = label
                            mExpanded = false
                        })
                    }
                }
            }

            val mContext = LocalContext.current
            val mYear: Int
            val mMonth: Int
            val mDay: Int
            val mCalendar = Calendar.getInstance()

            mYear = mCalendar.get(Calendar.YEAR)
            mMonth = mCalendar.get(Calendar.MONTH)
            mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
            mCalendar.time = Date()


            val mDatePickerDialog = DatePickerDialog(
                mContext,
                { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                    mDate.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
                }, mYear, mMonth, mDay
            )

            //DatePicker
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = if (mDate.value.isEmpty()) "Selected Date" else " ${mDate.value}",
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.size(8.dp))

                Button(onClick = {
                    mDatePickerDialog.show()
                    Log.e("inside", "->" + mDate)
                }, colors = ButtonDefaults.buttonColors(colorResource(id = R.color.purple_500))) {
                    Text(text = "Calender", color = Color.White)
                }
            }

            // Bottom button
            Button(
                onClick = {
                    onEventSent(
                        DetailsContract.Event.AddExpense(
                            amount = amount,
                            category = mSelectedText,
                            selectedDate = mDate.value
                        )
                    )
                },
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
}

@Composable
fun DropdownMenuItemText(text: String) {
    Text(text = text)
}

@Preview(showBackground = true)
@Composable
fun Temp1() {
    ExpenseManagerTheme {
        DetailsScreen(
            onEventSent = {},
            hiltViewModel<NavigationViewModel>(),
            2,
            hiltViewModel<DetailsViewModel>()
        )
    }
}
