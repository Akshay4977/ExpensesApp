package com.example.expensemanager.networking

import com.example.expensemanager.models.GetListResponse
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("objects")
    suspend fun getList(): Response<List<GetListResponse.Item>>

    @GET("objects/{index}")
    suspend fun getItem(@Path("index") index: Int): Response<GetListResponse.Item>
}