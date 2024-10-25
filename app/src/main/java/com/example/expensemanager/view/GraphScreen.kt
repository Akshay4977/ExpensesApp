package com.example.expensemanager.view


import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.example.expensemanager.SIDE_EFFECT_KEY
import com.example.expensemanager.contracts.GraphContract
import com.example.expensemanager.models.Expense
import com.example.expensemanager.navigation.NavigationViewModel
import com.example.expensemanager.viewmodels.GraphViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlin.math.cos
import kotlin.math.sin

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GraphScreen(
    onEventSent: (event: GraphContract.Event) -> Unit,
    navigationViewModel: NavigationViewModel,
    userId: Int,
    graphViewModel: GraphViewModel
) {

    val state = graphViewModel.viewState.value
    val dataList = state.itemList
    val effectFlow = graphViewModel.effect

    LaunchedEffect(Unit) {
        graphViewModel.getResult(userId)
    }
    LaunchedEffect(SIDE_EFFECT_KEY) {
        merge(effectFlow).onEach { effect ->
            when (effect) {
                else -> {}
            }
        }.collect()
    }

    Column {
        if (dataList.isNotEmpty()) {

            MyPieChart(dataList)
        }
    }
}

@Composable
fun MyPieChart(dataList: List<Expense>) {
    val colorList = listOf(Color.Cyan, Color.Red, Color.Green, Color.Cyan, Color.Yellow)
    var i = 0
    val pieChartData = mutableListOf<PieChartData>()

    val sumByCategory = dataList
        .groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.price } }


    sumByCategory.forEach {
        pieChartData.add(PieChartData(it.value.toInt(), colorList.get(i), it.key))
        i++
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val duration = dataList.get(0).date.substring(2, dataList.get(0).date.length)
        Text(text = duration)

        Spacer(modifier = Modifier.height(16.dp))

        PieChart(pieChartData)
    }
}

@SuppressLint("ResourceAsColor")
@Composable
fun PieChart(pieChartData: List<PieChartData>) {
    val total = pieChartData.sumOf { it.percentage }
    var startAngle = 0f

    Canvas(
        modifier = Modifier
            .size(200.dp)
    ) {
        pieChartData.forEach { data ->
            val sweepAngle = (data.percentage.toFloat() / total.toFloat()) * 360f

            drawArc(
                color = data.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true
            )

            val labelAngle = startAngle + sweepAngle / 2
            val x =
                (size.width / 2) + (size.width / 3) * cos(Math.toRadians(labelAngle.toDouble())).toFloat()
            val y =
                (size.height / 2) + (size.height / 3) * sin(Math.toRadians(labelAngle.toDouble())).toFloat()


            drawContext.canvas.nativeCanvas.drawText(
                data.category, x, y, Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 30f
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )

            startAngle += sweepAngle
        }
    }
}

data class PieChartData(
    val percentage: Int,
    val color: Color,
    val category: String
)
