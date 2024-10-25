package com.example.expensemanager.usecase

import com.example.expensemanager.repository.GetListRepository
import javax.inject.Inject

class GetItemUseCase @Inject constructor(
    private val getListRepository: GetListRepository
) {

    suspend fun getItem(index: Int) = getListRepository.getItem(index)
}