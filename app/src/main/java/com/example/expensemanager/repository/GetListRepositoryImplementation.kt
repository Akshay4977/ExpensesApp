package com.example.expensemanager.repository

import com.example.expensemanager.models.GetListResponse
import com.example.expensemanager.networking.ApiService
import com.example.expensemanager.networking.ApiUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetListRepositoryImplementation @Inject constructor(
    private val apiService: ApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GetListRepository {

    override suspend fun getList(): Result<List<GetListResponse.Item>> {
        return ApiUtil.makeApiCall(dispatcher) {
            val response = apiService.getList()
            ApiUtil.handleResponse(response).run {
                return@makeApiCall this
            }
        }
    }

    override suspend fun getItem(index: Int): Result<GetListResponse.Item> {
        return ApiUtil.makeApiCall(dispatcher) {
            val response = apiService.getItem(index)
            ApiUtil.handleResponse(response).run {
                return@makeApiCall this
            }
        }
    }
}
