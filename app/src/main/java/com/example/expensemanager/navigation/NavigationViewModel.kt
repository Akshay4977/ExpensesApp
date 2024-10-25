package com.example.expensemanager.navigation

import android.util.Log
import com.example.expensemanager.BaseViewModel
import com.example.expensemanager.usecase.GetApplicationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class NavigationViewModel @Inject constructor(private val getAuthTokenUseCase: GetApplicationUseCase) :
    BaseViewModel<NavigationContract.Event, NavigationContract.State, NavigationContract.Effect>() {
    override fun setInitialState(): NavigationContract.State {
        return NavigationContract.State(isLoading = false)
    }


    override fun handleEvents(event: NavigationContract.Event) {

    }

    init {
        setStart()
    }

    private fun setStart() {
        Log.e("inside", "started")
        //Log.e("inside", "auth token"+ getAuthTokenUseCase.execute())
    }
}