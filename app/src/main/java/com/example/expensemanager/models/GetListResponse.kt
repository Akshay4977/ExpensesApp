package com.example.expensemanager.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetListResponse(
    val list: List<Item>
) {
    @Serializable
    data class Item(
        @SerialName("id")
        val id: String = "",
        @SerialName("name")
        val name: String = "",
        @SerialName("data")
        val data: Details
    )

    @Serializable
    data class Details(
        @SerialName("year")
        val year: Int,
        @SerialName("price")
        val price: Double,
        @SerialName("CPU model")
        val cpu: String,
    )
}
