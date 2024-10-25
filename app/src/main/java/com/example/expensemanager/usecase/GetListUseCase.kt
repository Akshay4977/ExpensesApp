package com.example.expensemanager.usecase

import com.example.expensemanager.repository.GetListRepository
import javax.inject.Inject

class GetListUseCase @Inject constructor(
    private val getListRepository: GetListRepository
) {

    suspend fun execute() = getListRepository.getList()

}