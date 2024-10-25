package com.example.expensemanager.repository

import com.example.expensemanager.models.GetListResponse

interface GetListRepository {

    suspend fun getList(): Result<List<GetListResponse.Item>>

    suspend fun getItem(index: Int): Result<GetListResponse.Item>
}